package com.subgarden.airbrush.loaders


/**
 * A base64 encoded JPEG. Typically very small.
 *
 * @param base64 The base64 encoded JPEG
 * @param header An optional header that can be used to reduce the size of the base64 payload
 * @param base64DecodeFlag Optional flag to override the Base64 decode flag set by the TinyThumbDecoder.
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
data class TinyThumb @JvmOverloads constructor(
        val base64: String,
        val header: String = "",
        val base64DecodeFlag: Int = -1) {
    init {
        require(base64.isNotEmpty()) { "Missing data. Base64 string is empty." }
    }
}