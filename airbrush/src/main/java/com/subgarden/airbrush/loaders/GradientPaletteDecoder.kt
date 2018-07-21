package com.subgarden.airbrush.loaders

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource
import com.subgarden.airbrush.AirBrush
import kotlin.system.measureTimeMillis

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class GradientPaletteDecoder(var context: Context, private val bitmapPool: BitmapPool) : ResourceDecoder<GradientPalette, BitmapDrawable>{

    private var resources: Resources = context.resources

    override fun handles(source: GradientPalette, options: Options): Boolean {
        return true
    }

    override fun decode(gradientPalette: GradientPalette, width: Int, height: Int, options: Options?): Resource<BitmapDrawable> {
        lateinit var resource: Resource<BitmapDrawable>
        val t = measureTimeMillis {
            // Gradients can be scaled down drastically and still look descent. Later the image view
            // can scale it up using hardware acceleration.
            val sampleSize = 50
            val bitmap = AirBrush(context).getGradient(gradientPalette, width/sampleSize, height/sampleSize)
            val bitmapResource = BitmapResource.obtain(bitmap, bitmapPool)!!

            resource = LazyBitmapDrawableResource.obtain(resources, bitmapPool, bitmapResource.get())
        }
        Log.d("AirBrush", "Decode took $t ms")

        return resource
    }
}

