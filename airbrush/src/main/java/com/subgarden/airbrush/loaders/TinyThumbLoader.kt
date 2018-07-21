package com.subgarden.airbrush.loaders

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey

/**
 * @author Fredrik Larsen (fredrik@zedge.net)
 */
class TinyThumbLoader : ModelLoader<TinyThumb, TinyThumb> {

    override fun buildLoadData(model: TinyThumb, width: Int, height: Int, options: Options): ModelLoader.LoadData<TinyThumb>? {
        return ModelLoader.LoadData(ObjectKey(model.toString()), TinyThumbDataFetcher(model))
    }

    override fun handles(model: TinyThumb): Boolean {
        return model.base64.isNotEmpty()
    }

    class Factory : ModelLoaderFactory<TinyThumb, TinyThumb> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<TinyThumb, TinyThumb> = TinyThumbLoader()

        override fun teardown() {
            // Do nothing.
        }
    }
}