package com.subgarden.airbrush

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule


/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
@GlideModule
class GlideModule : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(Palette::class.java, Palette::class.java, PaletteModelLoader.Factory())
        registry.append(Palette::class.java, BitmapDrawable::class.java, PaletteDecoder(context, glide.bitmapPool))
    }

}