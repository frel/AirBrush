package com.subgarden.airbrushProject

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.subgarden.airbrush.AirBrush
import com.subgarden.airbrush_project.R
import java.util.*

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */
open class GridAdapter(data: ArrayList<Item>) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: ImageView
        lateinit var handler: Handler
    }

    private var data = ArrayList<Item>()

    init {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.grid_layout_item, parent, false)
        val halfOfWidth = parent.resources.displayMetrics.widthPixels / 3
        val halfOfHeight = parent.resources.displayMetrics.heightPixels / 3

        val holder = ViewHolder(view)
        holder.image = view.findViewById(R.id.image)
        holder.image.layoutParams.height = halfOfHeight
        holder.image.layoutParams.width = halfOfWidth
        holder.handler = Handler(Looper.getMainLooper())

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Remove the pending transitions. Don't use this approach in production.
        holder.handler.removeCallbacksAndMessages(null)

        val item = data[position]

        if (item.blurredPlaceholder != null) {

            val image = item.image
            val blurredPlaceholder = item.blurredPlaceholder

            val transitionDrawable = TransitionDrawable(arrayOf<Drawable>(BitmapDrawable(holder.image.resources, blurredPlaceholder), BitmapDrawable(holder.image.resources, image)))
            holder.image.setImageDrawable(transitionDrawable)
        } else if (item.palette != null) {
            if (holder.image.measuredHeight == 0 || holder.image.measuredHeight == 0) {

                holder.image.afterMeasured {
                    val palette = item.palette
                    val image = item.image

                    AirBrush().getDrawable(holder.image, image, palette)
                }
            } else {
                val palette = item.palette
                val image = item.image

                AirBrush().getDrawable(holder.image, image, palette)
            }


        }
        transitionToImageDelayed(holder)
    }

    inline fun <T: View> T.afterMeasured(crossinline function: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    function()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun transitionToImageDelayed(holder: ViewHolder) {
        holder.handler.postDelayed({
            val drawable = holder.image.drawable
            if (drawable is TransitionDrawable) {
                drawable.startTransition(300)

                holder.handler.postDelayed({ transitionToGradientDelayed(holder) }, 2000)
            }
        }, 2000)
    }

    private fun transitionToGradientDelayed(holder: ViewHolder) {
        holder.handler.postDelayed({
            val drawable = holder.image.drawable
            if (drawable is TransitionDrawable) {
                drawable.reverseTransition(300)
                holder.handler.postDelayed({ transitionToImageDelayed(holder) }, 2000)
            }
        }, 2000)
    }

}