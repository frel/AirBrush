package com.subgarden.airbrush

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.support.annotation.WorkerThread
import android.support.v7.graphics.Palette
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.RenderScript
import android.support.v8.renderscript.ScriptIntrinsicBlur
import com.subgarden.airbrush.loaders.GradientPalette

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class AirBrush(val context: Context) {

    private lateinit var inAllocation: Allocation
    private lateinit var outAllocation: Allocation
    private lateinit var bitmapOut: Bitmap

    companion object {
        /** The reusable RenderScript instance */
        private var renderScript: RenderScript? = null

        private fun getRenderScript(context: Context) : RenderScript {
            if (renderScript == null) {
                renderScript = RenderScript.create(context)
            }
            return renderScript!!
        }

        /** Releases any static references held by AirBrush. */
        fun cleanup() {
            renderScript = null
        }

        /**
         * Convenience method to get a palette from a bitmap. This code is not very efficient.
         */
        fun getPalette(bitmap: Bitmap): GradientPalette {
            val divisions = 6
            val width = bitmap.width - 1
            val height = bitmap.height - 1

            val upperLeftPoint = Point(width / divisions, height / divisions)
            val upperRightPoint = Point(width - width / divisions, height / divisions)

            val lowerLeftPoint = Point(width / divisions, height - height / divisions)
            val lowerRightPoint = Point(width - width / divisions, height - height / divisions)

            val upperLeftColor = getDominantColorFromPoint(bitmap, upperLeftPoint)
            val upperRightPixel = getDominantColorFromPoint(bitmap, upperRightPoint)
            val lowerLeftPixel = getDominantColorFromPoint(bitmap, lowerLeftPoint)
            val lowerRightPixel = getDominantColorFromPoint(bitmap, lowerRightPoint)

            return GradientPalette(upperLeftColor, upperRightPixel, lowerRightPixel, lowerLeftPixel)
        }

        private fun getDominantColorFromPoint(bitmap: Bitmap, upperLeftPoint: Point): Int {
            val crop = Bitmap.createBitmap(bitmap, upperLeftPoint.x, upperLeftPoint.y, 25, 25)

            val color = Palette.from(crop).generate().getDominantColor(Color.GRAY)
            crop.recycle()
            return color
        }

        fun blur(context: Context, image: Bitmap, scale: Float, radius: Float): Bitmap {
            val width = Math.round((image.width * scale))
            val height = Math.round((image.height * scale))

            val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val renderScript = getRenderScript(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            val tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)
            theIntrinsic.setRadius(radius)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        }

    }

    @WorkerThread
    fun getGradient(palette: GradientPalette,
                    width: Int,
                    height: Int) : Bitmap {
        // Gradients look unacceptable in RGB565
        bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val renderScript = RenderScript.create(context)
        // Allocate buffers
        inAllocation = Allocation.createFromBitmap(renderScript, bitmapOut)
        outAllocation = Allocation.createFromBitmap(renderScript, bitmapOut)

        val script = ScriptC_palette(renderScript)

        script._gIn = inAllocation
        script._gOut = outAllocation
        script._gScript = script

        script._gTopLeftColor = palette.topLeft
        script._gTopRightColor = palette.topRight
        script._gBottomRightColor = palette.bottomRight
        script._gBottomLeftColor = palette.bottomLeft

        script._gWidthPixels = width
        script._gHeightPixels = height
        script.invoke_filter()

        outAllocation.copyTo(bitmapOut)

        script.destroy()
        renderScript.destroy()
        return bitmapOut
    }

}
