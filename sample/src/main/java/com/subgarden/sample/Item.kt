package com.subgarden.sample

import android.support.annotation.DrawableRes
import com.subgarden.airbrush.loaders.GradientPalette
import com.subgarden.airbrush.loaders.TinyThumb

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
sealed class Item(@DrawableRes val resourceId: Int)
class GradientPaletteItem(@DrawableRes resourceId: Int, val palette: GradientPalette) : Item(resourceId)
class TinyThumbItem(@DrawableRes resourceId: Int, val thumb: TinyThumb) : Item(resourceId)