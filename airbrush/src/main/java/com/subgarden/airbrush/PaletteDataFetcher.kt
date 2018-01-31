package com.subgarden.airbrush

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class PaletteDataFetcher(private val palette: Palette) : DataFetcher<Palette> {

    override fun loadData(priority: Priority?, callback: DataFetcher.DataCallback<in Palette>) {
        callback.onDataReady(palette)
    }

    override fun cleanup() {
        // Do nothing.
    }

    override fun cancel() {
        // Do nothing.
    }

    override fun getDataClass(): Class<Palette> = Palette::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL
}