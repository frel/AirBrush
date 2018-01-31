package com.subgarden.airbrush.loaders
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource

typealias Base64String = String

/**
 * Decodes a base64 encoded JPEG into a BitmapDrawable.
 *
 * In order to optimise for performance, the size of the resulting bitmap is smaller than the view,
 * so CENTER_CROP should be used for the resulting view.
 *
 * Hardware acceleration (for the upscaling) should be used to render the final view.
 *
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class TinyThumbDecoder(
        private var context: Context,
        private val bitmapPool: BitmapPool,
        private val bitmapProvider: ((thumb: TinyThumb, width: Int, height: Int) -> Bitmap)? = null,
        private val blurProvider: (source: Bitmap) -> Bitmap)
    : ResourceDecoder<TinyThumb, BitmapDrawable> {

    override fun handles(source: TinyThumb, options: Options): Boolean {
        return source.base64.isNotEmpty()
    }

    override fun decode(source: TinyThumb, width: Int, height: Int, options: Options): Resource<BitmapDrawable>? {
        // Down sample the blurred bitmap to save resources. The later hardware upscale will look almost identical anyway.
        val sampleSize = 6
        val bitmap = bitmapProvider?.invoke(source, width / sampleSize, height / sampleSize)
                ?: generateBitmap(source, width / sampleSize, height / sampleSize)

        return LazyBitmapDrawableResource.obtain(context.resources, bitmapPool, bitmap)
    }

    /**
     * Generates a blurred bitmap based on the thumb.
     * The size of the bitmap is given by the provided width and height.
     */
    private fun generateBitmap(thumb: TinyThumb, width: Int, height: Int): Bitmap {
        val base64 = if (thumb.header.isNotBlank()) prependHeader(thumb) else thumb.base64
        val bytes = Base64.decode(base64, Base64.URL_SAFE)

        val options = BitmapFactory.Options().apply {
            // RGB565 will take less memory, but the resulting gradient is horrible.
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        // Convert the byte array into a bitmap.
        val source = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

        // If the thumb is square or landscape, center crop it. Otherwise leave it as is.
        val centerCrop = crop(source, width, height, 0.5f, 1f)
        return blurProvider(centerCrop)
    }

    /**
     * Prepends the header to the base64 string. This would normally be done to reduce the size of
     * the payload where the header would be the same for all items
     */
    private fun prependHeader(thumb: TinyThumb): Base64String {
        TODO("Not implemented. Want to contribute?")
    }

    /**
     * Returns a new Bitmap (from the pool) with a crop effect depending on the crop anchor given. 0.5f is like
     * [android.widget.ImageView.ScaleType.CENTER_CROP]. The crop anchor will be be nudged
     * so the entire cropped bitmap will fit inside the src. May return the input bitmap if no
     * scaling is necessary.
     *
     *
     * @param source original bitmap of any size
     * @param width desired width in px
     * @param height desired height in px
     * @param horizontalCenterPercent determines which part of the src to crop from.
     * Range from 0.0f to 1.0f. The value determines which part of the src maps to the
     * horizontal center of the resulting bitmap.
     * @param verticalCenterPercent determines which part of the src to crop from.
     * Range from 0.0f to 1.0f. The value determines which part of the src maps to the
     * vertical center of the resulting bitmap.
     * @return a copy of src conforming to the given width and height, or src itself if it already
     * matches the given width and height. The copy is recycled from the pool if possible.
     */
    private fun crop(source: Bitmap,
             width: Int,
             height: Int,
             horizontalCenterPercent: Float,
             verticalCenterPercent: Float): Bitmap {
        if (horizontalCenterPercent < 0 || horizontalCenterPercent > 1
                || verticalCenterPercent < 0 || verticalCenterPercent > 1) {
            throw IllegalArgumentException("horizontalCenterPercent and verticalCenterPercent " +
                    "must be between 0.0f and " + "1.0f, inclusive.")
        }
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Exit early if no resize/crop needed
        if (width == sourceWidth && height == sourceHeight) {
            return source
        }

        val scale = Math.max(
                width.toFloat() / sourceWidth,
                height.toFloat() / sourceHeight)

        val sourceCroppedWidth: Int
        val sourceCroppedHeight: Int
        sourceCroppedWidth = Math.round(width / scale)
        sourceCroppedHeight = Math.round(height / scale)

        var sourceX: Int
        var sourceY: Int
        sourceX = (sourceWidth * horizontalCenterPercent - sourceCroppedWidth / 2).toInt()
        sourceY = (sourceHeight * verticalCenterPercent - sourceCroppedHeight / 2).toInt()

        // Nudge sourceX and sourceY to be within the bounds of src
        sourceX = Math.max(Math.min(sourceX, sourceWidth - sourceCroppedWidth), 0)
        sourceY = Math.max(Math.min(sourceY, sourceHeight - sourceCroppedHeight), 0)

        // Since we're drawing over the whole bitmap, we can get a dirty bitmap from the pool
        // which is slightly faster as erasing the bitmap is not necessary.
        val croppedBitmap = bitmapPool.getDirty(width, height, Bitmap.Config.ARGB_8888)
        Canvas(croppedBitmap).apply {
            val sourceRect = Rect(sourceX, sourceY, sourceX + sourceCroppedWidth, sourceY + sourceCroppedHeight)
            val destinationRect = Rect(0, 0, width, height)
            drawBitmap(source, sourceRect, destinationRect, Paint().apply { isFilterBitmap = true })
        }

        return croppedBitmap
    }
}