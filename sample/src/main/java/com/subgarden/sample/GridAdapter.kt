package com.subgarden.sample

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.subgarden.airbrush_project.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.grid_layout_item.*

/**
 * @author Fredrik Larsen (fredrik@subgarden.com)
 */
open class GridAdapter(
        private var data: List<Item>,
        private val requestManager: RequestManager)
    : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.grid_layout_item, parent, false)
        val width = parent.resources.displayMetrics.widthPixels / SimpleActivity.SPAN_COUNT
        val height = parent.resources.displayMetrics.heightPixels / SimpleActivity.SPAN_COUNT

        return ViewHolder(view).apply {
            image.layoutParams.width = width
            image.layoutParams.height = height
        }
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]

        // Crossfade between the thumbnail and the final image
        val crossFade = DrawableTransitionOptions().crossFade(750)

        // For demo purposes we skip memory/disk cache
        val options = RequestOptions().apply {
            skipMemoryCache(true)
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }

        val thumbnail: Any  = when (item) {
            is GradientPaletteItem -> item.palette
            is TinyThumbItem -> item.thumb
        }

        // Load the image using the URI.
        // The delayed interceptor will return the drawable resource id as a jpeg
        requestManager.load("http://airbrush/${item.resourceId}")
                // Notice how we load the types directly as a thumbnail in Glide
                .thumbnail(requestManager.load(thumbnail))
                .transition(crossFade)
                .apply(options)
                .into(holder.image)
    }

}