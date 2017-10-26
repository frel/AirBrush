package com.subgarden.airbrushProject

import android.graphics.Bitmap

import com.subgarden.airbrush.Palette

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */
class Item {

    val palette: Palette?
    val blurredPlaceholder: Bitmap?
    val image: Bitmap

    constructor(image: Bitmap, blurredPlaceholder: Bitmap) {
        this.blurredPlaceholder = blurredPlaceholder
        this.image = image
        this.palette = null
    }

    constructor(image: Bitmap, palette: Palette) {
        this.palette = palette
        this.image = image
        this.blurredPlaceholder = null
    }
}
