package com.subgarden.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.subgarden.airbrush.AirBrush
import com.subgarden.airbrush.loaders.GradientPalette
import com.subgarden.airbrush.loaders.TinyThumb
import com.subgarden.airbrush_project.R
import kotlinx.android.synthetic.main.activity_simple.*


class SimpleActivity : AppCompatActivity() {

    companion object {
        const val SPAN_COUNT = 3
    }

    private lateinit var adapter: GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        gradientPaletteRadioButton.text = GradientPalette::class.java.simpleName
        tinyThumbRadioButton.text = TinyThumb::class.java.simpleName

        val dataSource = StaticDataSource(this)

        gradientPaletteRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                adapter = GridAdapter(dataSource.gradientPaletteData, GlideApp.with(this))
                recyclerView.adapter = adapter
            }
        }

        tinyThumbRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                adapter = GridAdapter(dataSource.tinyThumbData, GlideApp.with(this))
                recyclerView.adapter = adapter
            }
        }

        // Check the button and toggle the adapter to be setup
        radioGroup.check(gradientPaletteRadioButton.id)

        val layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerView.layoutManager = layoutManager

        reloadButton.setOnClickListener {
            // Invalidate all items.
            // Normally notifyDataSetChanged would be used, but Glide's crossfade sometimes continues
            // after onBind is called again, causing a glitch.
            // This is normally not a problem, but for demo purposes it looks less than ideal.
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // After spraying paint all over the place it's time to clean up.
        // Luckily this is quick and easy. Normally you'd call cleanup() when you're down using AirBrush
        // for a good while. Note that it's safe to use AirBrush again, even after cleanup() has been called.
        AirBrush.cleanup()
    }
}
