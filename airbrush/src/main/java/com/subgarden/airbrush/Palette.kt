package com.subgarden.airbrush

import android.support.annotation.ColorInt

/**
 * A color palette describing the color of the four corners of an image.
 *
 * @author Fredrik Larsen <f@subgarden.com>
 */
class Palette(@param:ColorInt @field:ColorInt var topLeft: Int,
              @param:ColorInt @field:ColorInt var topRight: Int,
              @param:ColorInt @field:ColorInt var bottomRight: Int,
              @param:ColorInt @field:ColorInt var bottomLeft: Int)
