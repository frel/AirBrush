package com.subgarden.airbrushProject

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.subgarden.airbrush.AirBrush
import com.subgarden.airbrush_project.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

val tag: String = SimpleActivity::class.java.simpleName

class SimpleActivity : AppCompatActivity() {
    
    private var imageWidth: Int = 0
    private var imageHeight: Int = 0
    private var tinyImage: Int = 0
    private lateinit var data : ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        val spanCount = 3
        imageWidth = resources.displayMetrics.widthPixels / spanCount
        imageHeight = resources.displayMetrics.widthPixels / spanCount
        tinyImage = 5
        data = initData()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val gridAdapter = GridAdapter(data)
        recyclerView.adapter = gridAdapter

        val layoutManager = GridLayoutManager(this, spanCount)
        recyclerView.layoutManager = layoutManager
    }

    // Prepare some dummy data for recycler view
    private fun initData(): ArrayList<Item> {
        val imageItems = ArrayList<Item>()

        val resources = intArrayOf(R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5, R.drawable.test6, R.drawable.test7, R.drawable.test8, R.drawable.test9, R.drawable.test10, R.drawable.test11, R.drawable.test12)

        var bitmap: Bitmap
        for (resource in resources) {
            bitmap = decodeBitmap(resource)
            val palette = AirBrush.getPalette(bitmap)
            imageItems.add(Item(bitmap, palette))
        }
        val scale = 3
        val radius = 4
        var bitmapTiny: Bitmap

        for (i in resources.indices) {
            bitmapTiny = decodeTinyBitmap(resources[i])
            saveToFile(bitmapTiny, "tiny$i.jpg")

            bitmap = decodeBitmap(resources[i])
            imageItems.add(Item(bitmap, AirBrush.blur(this, bitmapTiny, scale, radius)))
        }

        var totalSize: Long = 0
        for (i in imageItems.indices) {
            val image = imageItems[i].image
            val height = image.height
            val width = image.width
            val bytes = image.byteCount.toLong()
            val kiloBytes = bytes / 1024
            val megaBytes = bytes / 1024 / 1024
            Log.d(tag, "Dimensions: " + width + "x" + height + " Size: " + bytes + "B " + kiloBytes + "KB " + megaBytes + "MB")
            totalSize += bytes
        }

        Log.d(tag, "Total size of bitmaps in memory: " + totalSize / 1024 / 1024)
        return imageItems
    }

    private fun saveToFile(bitmapTiny: Bitmap, name: String) {

        val stream = ByteArrayOutputStream()
        bitmapTiny.compress(Bitmap.CompressFormat.JPEG, 10, stream)
        val byteArray = stream.toByteArray()

        try {

            val fileOuputStream = FileOutputStream(File(externalCacheDir, name))
            fileOuputStream.write(byteArray)
            fileOuputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun decodeBitmap(@DrawableRes resId: Int): Bitmap {
        return decodeSampledBitmapFromResourceUsingConfig(resources, resId, imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
    }

    private fun decodeTinyBitmap(@DrawableRes resId: Int): Bitmap {
        return decodeSampledBitmapFromResourceUsingConfig(resources, resId, tinyImage, tinyImage, Bitmap.Config.RGB_565)
    }
    
}

fun decodeSampledBitmapFromResourceUsingConfig(res: Resources,
                                               @DrawableRes resId: Int,
                                               reqWidth: Int,
                                               reqHeight: Int,
                                               config: Bitmap.Config): Bitmap {

    // First decode with inJustDecodeBounds=true to check dimensions

    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inScaled = false
    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    options.inPreferredConfig = config
    Log.d(tag, "inSampleSize = " + options.inSampleSize + " " + options.outHeight + "x" + options.outWidth)
    return BitmapFactory.decodeResource(res, resId, options)
}

private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
