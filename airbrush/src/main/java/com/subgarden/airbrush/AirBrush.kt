package com.subgarden.airbrush

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.RenderScript
import android.support.v8.renderscript.ScriptIntrinsicBlur

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class AirBrush(val context: Context) {

    private lateinit var inAllocation: Allocation
    private lateinit var outAllocation: Allocation
    private lateinit var script: ScriptC_gradient
    private lateinit var bitmapOut: Bitmap

    fun getGradient(palette: Palette, width: Int, height: Int) : Bitmap {
        createScript(width, height)
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

        // Copy to bitmap and invalidate image view
        outAllocation.copyTo(bitmapOut)
        return bitmapOut
    }

    private fun createScript(width: Int, height: Int) {
        // Initialize RS
        val rs = RenderScript.create(context)
        bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Allocate buffers
        inAllocation = Allocation.createFromBitmap(rs, bitmapOut)
        outAllocation = Allocation.createFromBitmap(rs, bitmapOut)

        // Load script
        script = ScriptC_gradient(rs)
    }

    companion object {

        /**
         * Convenience method to get a palette from am bitmap.
         */
        fun getPalette(bitmap: Bitmap): Palette {
            // Sample colors from the four corners. This should probably be an average, but for now this will do.
            // Note that we're not sampling the exact corner pixels, but slightly inset from the edges.
            val divisions = 6
            val width = bitmap.width - 1
            val height = bitmap.height - 1

            val upperLeftPoint = Point(width / divisions, height / divisions)
            val upperRightPoint = Point(width - width / divisions, height / divisions)

            val lowerLeftPoint = Point(width / divisions, height - height / divisions)
            val lowerRightPoint = Point(width - width / divisions, height - height / divisions)

            val upperLeftColor = getAverageColorFromPoint(bitmap, upperLeftPoint)
            val upperRightPixel = getAverageColorFromPoint(bitmap, upperRightPoint)
            val lowerLeftPixel = getAverageColorFromPoint(bitmap, lowerLeftPoint)
            val lowerRightPixel = getAverageColorFromPoint(bitmap, lowerRightPoint)

            return Palette(upperLeftColor, upperRightPixel, lowerRightPixel, lowerLeftPixel)
        }

        private fun getAverageColorFromPoint(bitmap: Bitmap, upperLeftPoint: Point): Int {
            val crop = Bitmap.createBitmap(bitmap, upperLeftPoint.x, upperLeftPoint.y, 25, 25)

            val color = android.support.v7.graphics.Palette.from(crop).generate().getDominantColor(Color.GRAY)
            crop.recycle()
            return color
        }

        private fun blur(context: Context, image: Bitmap, scale: Int, radius: Int): Bitmap {
            val width = Math.round((image.width * scale).toFloat())
            val height = Math.round((image.height * scale).toFloat())

            val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val rs = RenderScript.create(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(radius.toFloat())
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        }

    }

}
