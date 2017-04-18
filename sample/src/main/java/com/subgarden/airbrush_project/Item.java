package com.subgarden.airbrush_project;

import android.graphics.Bitmap;

import com.subgarden.airbrush.Palette;

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */
public class Item {

    final private Palette palette;
    final private Bitmap image;
    final private Bitmap blurredPlaceholder;

    public Item(Bitmap image, Bitmap blurredPlaceholder) {
        this.blurredPlaceholder = blurredPlaceholder;
        this.image = image;
        this.palette = null;
    }
    public Item(Bitmap image, Palette palette) {
        this.palette = palette;
        this.image = image;
        this.blurredPlaceholder = null;
    }

    public Palette getPalette() {
        return palette;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getBlurredPlaceholder() {
        return blurredPlaceholder;
    }
}
