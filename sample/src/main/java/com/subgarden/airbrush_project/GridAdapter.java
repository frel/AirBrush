package com.subgarden.airbrush_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.subgarden.airbrush.AirBrush;
import com.subgarden.airbrush.Palette;

import java.util.ArrayList;

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private ArrayList<Item> data = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        Handler handler;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @SuppressWarnings("unchecked")
    public GridAdapter(ArrayList<Item> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                                                         .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.grid_layout_item, parent, false);
        int halfOfScreen = parent.getResources().getDisplayMetrics().widthPixels / 2;

        ViewHolder holder = new ViewHolder(view);
        holder.image = (ImageView) view.findViewById(R.id.image);
        holder.image.getLayoutParams().height = halfOfScreen;
        holder.image.getLayoutParams().width = halfOfScreen;
        holder.handler = new Handler(Looper.getMainLooper());

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Remove the pending transitions. Don't use this approach in production.
        holder.handler.removeCallbacksAndMessages(null);

        Item item = data.get(position);
        TransitionDrawable transitionDrawable = getTransitionDrawable(holder, item);
        holder.image.setImageDrawable(transitionDrawable);

        transitionToImageDelayed(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    private TransitionDrawable getTransitionDrawable(ViewHolder holder, Item item) {
        TransitionDrawable transitionDrawable;
        if (item.getBlurredPlaceholder() != null) {

            Bitmap image = item.getImage();
            Bitmap blurredPlaceholder = item.getBlurredPlaceholder();

            transitionDrawable = new TransitionDrawable(new Drawable[] {
                    new BitmapDrawable(holder.image.getResources(), blurredPlaceholder),
                    new BitmapDrawable(holder.image.getResources(), image)
            });
        } else {
            Palette palette = item.getPalette();
            Bitmap image = item.getImage();
            Drawable gradient = AirBrush.getGradient(holder.image, palette);

            transitionDrawable= new TransitionDrawable(new Drawable[] {
                    gradient,
                    new BitmapDrawable(holder.image.getResources(), image)
            });

        }
        return transitionDrawable;
    }


    protected void transitionToImageDelayed(final ViewHolder holder) {
        holder.handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Drawable drawable = holder.image.getDrawable();
                if (drawable instanceof TransitionDrawable) {
                    ((TransitionDrawable) drawable).startTransition(300);

                    holder.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            transitionToGradientDelayed(holder);
                        }
                    }, 2000);
                }
            }
        }, 2000);
    }

    protected void transitionToGradientDelayed(final ViewHolder holder) {
        holder.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = holder.image.getDrawable();
                if (drawable instanceof TransitionDrawable) {
                    ((TransitionDrawable)drawable).reverseTransition(300);
                    holder.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            transitionToImageDelayed(holder);
                        }
                    }, 2000);
                }
            }
        }, 2000);
    }

}