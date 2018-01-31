package com.subgarden.sample

import android.support.annotation.DrawableRes
import com.subgarden.airbrush.Palette

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
data class Item(@DrawableRes val resourceId: Int, val palette: Palette)