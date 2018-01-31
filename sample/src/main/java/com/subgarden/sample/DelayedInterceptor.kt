package com.subgarden.sample

import android.content.Context
import android.graphics.Bitmap
import okhttp3.*
import java.io.ByteArrayOutputStream

/**
 * Interceptor used with OkHttp and Glide to serve test images with a delay.
 * The last path of the URI is used to resolve the drawable resource id.
 *
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class DelayedInterceptor(private val context: Context,
                         private val delayMs: Long) : Interceptor {

    companion object {
        private val MEDIA_IMAGE = MediaType.parse("application/image")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the resource id from the url
        val url = chain.request().url()
        val resourceId = url.pathSegments().last().toInt()

        // Delay the response
        Thread.sleep(delayMs)

        // Create the jpeg byte array and build the response
        val jpegByteArray = getJpegByteArray(resourceId)
        return Response.Builder()
                .body(ResponseBody.create(MEDIA_IMAGE, jpegByteArray))
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .build()
    }

    private fun getJpegByteArray(resourceId: Int): ByteArray {
        val imageWidth = context.resources.displayMetrics.widthPixels / SimpleActivity.SPAN_COUNT
        val imageHeight = context.resources.displayMetrics.widthPixels / SimpleActivity.SPAN_COUNT
        val bitmap = BitmapUtil.decodeBitmapFromResource(context, resourceId, imageWidth, imageHeight)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val jpegByteArray = stream.toByteArray()
        stream.close()
        return jpegByteArray
    }

}