package com.subgarden.airbrush.loaders

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class GradientPaletteDataFetcher(private val gradientPalette: GradientPalette) : DataFetcher<GradientPalette> {

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in GradientPalette>) {
        callback.onDataReady(gradientPalette)
    }

    override fun cleanup() {
        // Do nothing.
    }

    override fun cancel() {
        // Do nothing.
    }

    override fun getDataClass(): Class<GradientPalette> = GradientPalette::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL
}
