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
open class GridAdapter(private var data: List<Item>,
                       private val requestManager: RequestManager) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.grid_layout_item, parent, false)
        val width = parent.resources.displayMetrics.widthPixels / 3
        val height = parent.resources.displayMetrics.heightPixels / 3

        val holder = ViewHolder(view)
        holder.image.layoutParams.width = width
        holder.image.layoutParams.height = height

        return holder
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]

        // Crossfade between the thumbnail and the final image
        val crossFade = DrawableTransitionOptions().crossFade(750)

        val options = RequestOptions().apply {
            skipMemoryCache(true)
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }

        // Load the image using the URI.
        // The delayed interceptor will return the drawable resource id as a jpeg
        requestManager.load("http://airbrush/${item.resourceId}")
                // Load thumbnail using the palette
                .thumbnail(requestManager.load(item.palette))
                .transition(crossFade)
                .apply(options)
                .into(holder.image)
    }

}