package com.subgarden.airbrush.loaders


/**
 * A base64 encoded jpeg. Typically very small.
 *
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
data class TinyThumb(val base64: String, val header: String = "") {
    init {
        require(base64.isNotEmpty()) { "Missing data. Base64 string is empty." }
    }
}