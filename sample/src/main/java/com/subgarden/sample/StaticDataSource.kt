package com.subgarden.sample

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import android.util.Base64
import com.subgarden.airbrush.AirBrush
import com.subgarden.airbrush.loaders.TinyThumb
import com.subgarden.airbrush_project.R
import java.io.ByteArrayOutputStream

/**
 * For demo purposes
 *
 * @author Fredrik Larsen <f@subgarden.com>
 */
class StaticDataSource(val context: Context) {

    private val resources = intArrayOf(R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4,
            R.drawable.test5, R.drawable.test6, R.drawable.test7, R.drawable.test8,
            R.drawable.test9, R.drawable.test10, R.drawable.test11, R.drawable.test12)

    val gradientPaletteData = resources.map {
        val bitmap = decodeBitmap(it)
        val palette = AirBrush.getPalette(bitmap)
        GradientPaletteItem(it, palette)
    }

    val tinyThumbData = resources.map {
        val bitmap = decodeTinyBitmap(it)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val byteArray = stream.toByteArray()
        bitmap.recycle()

        val base64 = Base64.encodeToString(byteArray, Base64.URL_SAFE)
        val tinyThumb = TinyThumb(base64)
        TinyThumbItem(it, tinyThumb)
    }

    private fun decodeBitmap(@DrawableRes resId: Int): Bitmap {
        val imageWidth = context.resources.displayMetrics.widthPixels / SimpleActivity.SPAN_COUNT
        val imageHeight = context.resources.displayMetrics.widthPixels / SimpleActivity.SPAN_COUNT

        return BitmapUtil.decodeBitmapFromResource(context, resId, imageWidth, imageHeight)
    }

    private fun decodeTinyBitmap(@DrawableRes resId: Int): Bitmap {
        return BitmapUtil.decodeBitmapFromResource(context, resId, 10, 10)
    }
}
