package com.subgarden.airbrush

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey


/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class PaletteModelLoader : ModelLoader<Palette, Palette> {

    override fun buildLoadData(model: Palette, width: Int, height: Int, options: Options?): ModelLoader.LoadData<Palette>? {
        return ModelLoader.LoadData(ObjectKey(model.toString()), PaletteDataFetcher(model))
    }

    override fun handles(model: Palette): Boolean {
        return true
    }

    class Factory : ModelLoaderFactory<Palette, Palette> {

        override fun build(multiFactory: MultiModelLoaderFactory?): ModelLoader<Palette, Palette> = PaletteModelLoader()

        override fun teardown() {
            // Do nothing.
        }
    }
}
