package com.subgarden.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */
object BitmapUtil {

    fun decodeBitmapFromResource(context: Context,
                                 @DrawableRes resourceId: Int,
                                 width: Int,
                                 height: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, resourceId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height)
        options.inScaled = false

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeResource(context.resources, resourceId, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options,
                                      requiredMinWidth: Int,
                                      requiredMinHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > requiredMinHeight || width > requiredMinWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested min height and min width.
            while (halfHeight / inSampleSize >= requiredMinHeight && halfWidth / inSampleSize >= requiredMinWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}
