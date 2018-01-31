package com.subgarden.airbrush

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class PaletteDecoder(var context: Context, private val bitmapPool: BitmapPool) : ResourceDecoder<Palette, BitmapDrawable>{

    private var resources: Resources = context.resources

    override fun handles(source: Palette, options: Options): Boolean {
        return true
    }

    override fun decode(source: Palette, width: Int, height: Int, options: Options?): Resource<BitmapDrawable>? {
        val bitmap = AirBrush(context).getGradient(source, width, height)
        val bitmapResource = BitmapResource.obtain(bitmap, bitmapPool)

        return LazyBitmapDrawableResource.obtain(resources, bitmapPool, bitmapResource!!.get())
    }
}

