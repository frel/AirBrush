package com.subgarden.airbrush

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.AsyncTask
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.RenderScript
import android.support.v8.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */
class AirBrush {

    private var mInAllocation: Allocation? = null
    private var mOutAllocation: Allocation? = null
    private var mScript: ScriptC_gradient? = null
    private var mCurrentTask: RenderScriptTask? = null
    private var mBitmapOut: Bitmap? = null
    private var mCurrentBitmap: Int = 0
    private var mImageView: ImageView? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mBitmap: Bitmap? = null

    fun getDrawable(view: ImageView, bitmap: Bitmap, palette: Palette) {
        mImageView = view
        mBitmap = bitmap
        createScript(view)
        updateImage(0.5f, palette)
    }

    fun createScript(view: ImageView) {
        // Initialize RS
        val rs = RenderScript.create(view.context)
        mWidth = view.width
        mHeight = view.height

        mBitmapOut = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)

        // Allocate buffers
        mInAllocation = Allocation.createFromBitmap(rs, mBitmapOut!!)
        mOutAllocation = Allocation.createFromBitmap(rs, mBitmapOut!!)

        // Load script
        mScript = ScriptC_gradient(rs)
    }

    private inner class RenderScriptTask(val mPalette: Palette) : AsyncTask<Float, Void, Int>() {
        internal var issued: Boolean? = false
        override fun doInBackground(vararg values: Float?): Int {

            var index = -1
            if (!isCancelled) {
                issued = true
                index = mCurrentBitmap

                mScript!!._gIn = mInAllocation
                mScript!!._gOut = mOutAllocation
                mScript!!._gScript = mScript

                mScript!!._gTopLeftColor = mPalette.topLeft
                mScript!!._gTopRightColor = mPalette.topRight
                mScript!!._gBottomRightColor = mPalette.bottomRight
                mScript!!._gBottomLeftColor = mPalette.bottomLeft

                mScript!!._gWidthPixels = mWidth
                mScript!!._gHeightPixels = mHeight
                mScript!!.invoke_filter()

                // Copy to bitmap and invalidate image view
                mOutAllocation!!.copyTo(mBitmapOut!!)
                mCurrentBitmap = (mCurrentBitmap + 1) % NUM_BITMAPS
            }
            return index
        }

        internal fun updateView(result: Int?) {
            if (result != -1) {
                // Request UI update

                val transitionDrawable = TransitionDrawable(arrayOf<Drawable>(BitmapDrawable(mImageView!!.resources, mBitmapOut), BitmapDrawable(mImageView!!.resources, mBitmap)))
                mImageView!!.setImageDrawable(transitionDrawable)

                mImageView!!.invalidate()
            }
        }

        override fun onPostExecute(result: Int?) {
            updateView(result)
        }

        override fun onCancelled(result: Int?) {
            if (issued!!) {
                updateView(result)
            }
        }
    }

    /**
     * Invoke AsyncTask and cancel previous task. When AsyncTasks are piled up (typically in slow
     * device with heavy kernel), Only the latest (and already started) task invokes RenderScript
     * operation.
     */
    private fun updateImage(f: Float, palette: Palette) {
        if (mCurrentTask != null) {
            mCurrentTask!!.cancel(false)
        }

        mCurrentTask = RenderScriptTask(palette)
        mCurrentTask!!.execute(f)
    }

    companion object {

        val TAG = AirBrush::class.java.simpleName
        private val NUM_BITMAPS = 2

        fun getPalette(bitmap: Bitmap): Palette {

            if (bitmap == null) {
                throw IllegalStateException("Unable to get color palette. Bitmap is null!")
            }

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

        fun blur(context: Context, image: Bitmap, scale: Int, radius: Int): Bitmap {
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
