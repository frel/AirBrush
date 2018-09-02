package com.subgarden.sample

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.subgarden.airbrush.AirBrush
import com.subgarden.airbrush.loaders.TinyThumb
import com.subgarden.airbrush.loaders.TinyThumbDecoder
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
@GlideModule
open class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Setup the custom interceptor for serving delayed content
        val client = OkHttpClient.Builder()
                .addInterceptor(DelayedInterceptor(context, 3000))
                .build()

        // Replace the default glide url loader
        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)

        // This is how AirBrush's TinyThumb can be customised. E.g. blurring and the Base64 decode flag.
        val decoder = TinyThumbDecoder(context, glide.bitmapPool, Base64.URL_SAFE) { bitmap ->
            AirBrush.blur(context, bitmap, scale = 1f, radius = 20f)
        }
        registry.prepend(TinyThumb::class.java, BitmapDrawable::class.java, decoder)
    }
}