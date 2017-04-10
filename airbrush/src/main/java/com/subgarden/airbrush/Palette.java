package com.subgarden.airbrush;

import android.support.annotation.ColorInt;

/**
 * A color palette describing the color of the four corners of an image, and its center color.
 *
 * @author Fredrik Larsen <f@subgarden.com>
 */
public class Palette {
    public @ColorInt int topLeft;
    public @ColorInt int topRight;
    public @ColorInt int bottomLeft;
    public @ColorInt int bottomRight;

    public Palette(@ColorInt int topLeft,
                   @ColorInt int topRight,
                   @ColorInt int bottomLeft,
                   @ColorInt int bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

}
