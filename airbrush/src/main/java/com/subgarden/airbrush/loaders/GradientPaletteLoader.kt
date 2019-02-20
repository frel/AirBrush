package com.subgarden.airbrush.loaders

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey


/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
class PaletteModelLoader : ModelLoader<GradientPalette, GradientPalette> {

    override fun buildLoadData(model: GradientPalette, width: Int, height: Int, options: Options): ModelLoader.LoadData<GradientPalette>? {
        return ModelLoader.LoadData(ObjectKey(model.toString()), GradientPaletteDataFetcher(model))
    }

    override fun handles(model: GradientPalette): Boolean {
        return true
    }

    class Factory : ModelLoaderFactory<GradientPalette, GradientPalette> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GradientPalette, GradientPalette> = PaletteModelLoader()

        override fun teardown() {
            // Do nothing.
        }
    }
}
