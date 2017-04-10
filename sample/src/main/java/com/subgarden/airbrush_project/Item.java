package com.subgarden.airbrush_project;

import android.graphics.Bitmap;

import com.subgarden.airbrush.AirBrush;
import com.subgarden.airbrush.Palette;

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */
public class Item {

    final private Palette palette;
    final private Bitmap image;

    public Item(Bitmap image) {
        this.palette = AirBrush.getPalette(image);
        this.image = image;
    }

    public Palette getPalette() {
        return palette;
    }

    public Bitmap getImage() {
        return image;
    }
}
