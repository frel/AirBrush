package com.subgarden.airbrush.loaders

import android.support.annotation.ColorInt

/**
 * A color palette describing the color of the four corners of a gradient.
 *
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
data class GradientPalette(
        @ColorInt val topLeft: Int,
        @ColorInt val topRight: Int,
        @ColorInt val bottomRight: Int,
        @ColorInt val bottomLeft: Int)
