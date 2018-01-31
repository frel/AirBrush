package com.subgarden.airbrush

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule
import com.subgarden.airbrush.loaders.*

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
@GlideModule
class GlideModule : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(GradientPalette::class.java, GradientPalette::class.java, PaletteModelLoader.Factory())
        registry.append(GradientPalette::class.java, BitmapDrawable::class.java, GradientPaletteDecoder(context, glide.bitmapPool))

        registry.append(TinyThumb::class.java, TinyThumb::class.java, TinyThumbLoader.Factory())
        registry.append(TinyThumb::class.java, BitmapDrawable::class.java, TinyThumbDecoder(context, glide.bitmapPool) { bitmap ->
            AirBrush.blur(context, bitmap, scale = 1f, radius = 15f)
        })
    }

}