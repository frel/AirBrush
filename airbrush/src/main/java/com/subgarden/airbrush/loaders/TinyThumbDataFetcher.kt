package com.subgarden.airbrush.loaders

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class TinyThumbDataFetcher(private val thumb: TinyThumb) : DataFetcher<TinyThumb> {

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in TinyThumb>) {
        callback.onDataReady(thumb)
    }

    override fun cleanup() {
        // Do nothing.
    }

    override fun cancel() {
        // Do nothing.
    }

    override fun getDataClass(): Class<TinyThumb> = TinyThumb::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL
}