package com.subgarden.airbrush

import android.support.annotation.ColorInt

/**
 * A color palette describing the color of the four corners of an image.
 *
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
data class Palette(@param:ColorInt @field:ColorInt val topLeft: Int,
                   @param:ColorInt @field:ColorInt val topRight: Int,
                   @param:ColorInt @field:ColorInt val bottomRight: Int,
                   @param:ColorInt @field:ColorInt val bottomLeft: Int)
