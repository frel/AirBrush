package com.subgarden.sample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.subgarden.airbrush.AirBrush
import com.subgarden.airbrush_project.R
import kotlinx.android.synthetic.main.activity_simple.*


class SimpleActivity : AppCompatActivity() {

    companion object {
        const val SPAN_COUNT = 3
    }

    private val data: List<Item> get() {

        val resources = intArrayOf(R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4,
                                   R.drawable.test5, R.drawable.test6, R.drawable.test7, R.drawable.test8,
                                   R.drawable.test9, R.drawable.test10, R.drawable.test11, R.drawable.test12)

        return resources.map {
            val bitmap = decodeBitmap(it)
            val palette = AirBrush.getPalette(bitmap)
            Item(it, palette)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        val gridAdapter = GridAdapter(data, GlideApp.with(this))
        recyclerView.adapter = gridAdapter

        val layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerView.layoutManager = layoutManager

        reloadButton.setOnClickListener {
            recyclerView.adapter.notifyDataSetChanged()
        }
    }

    private fun decodeBitmap(@DrawableRes resId: Int): Bitmap {
        val imageWidth = resources.displayMetrics.widthPixels / SPAN_COUNT
        val imageHeight = resources.displayMetrics.widthPixels / SPAN_COUNT

        return BitmapUtil.decodeBitmapFromResource(this, resId, imageWidth, imageHeight)
    }
}
