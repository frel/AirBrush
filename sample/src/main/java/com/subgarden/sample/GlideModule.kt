package com.subgarden.sample

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.subgarden.airbrush.Palette
import com.subgarden.airbrush.PaletteDecoder
import com.subgarden.airbrush.PaletteModelLoader
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
@GlideModule
open class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Enable our custom Palette class to be loaded and decoded by Glide
        registry.append(Palette::class.java, Palette::class.java, PaletteModelLoader.Factory())
        registry.append(Palette::class.java, BitmapDrawable::class.java, PaletteDecoder(context, glide.bitmapPool))

        // Setup the custom interceptor for serving delayed content
        val client = OkHttpClient.Builder()
                .addInterceptor(DelayedInterceptor(context, 3000))
                .build()

        // Replace the default glide url loader
        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}