package com.subgarden.airbrush_project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.subgarden.airbrush.AirBrush;
import com.subgarden.airbrush.Palette;

import java.util.ArrayList;

/**
 * @author Fredrik Larsen <f@subgarden.com>
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Item> data = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Item> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int halfOfScreen = parent.getResources().getDisplayMetrics().widthPixels / 2;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.image.getLayoutParams().height = halfOfScreen;
            holder.image.getLayoutParams().width = halfOfScreen;
            holder.handler = new Handler(Looper.getMainLooper());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            // Remove the pending transitions. Don't use this approach in production.
            holder.handler.removeCallbacksAndMessages(null);
        }

        Item item = data.get(position);
        Palette palette = item.getPalette();
        Bitmap image = item.getImage();
        Drawable gradient = AirBrush.getGradient(holder.image, palette);

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                gradient,
                new BitmapDrawable(holder.image.getResources(), image)
        });
        holder.image.setImageDrawable(transitionDrawable);

        transitionToImageDelayed(holder);

        return convertView;
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

    static class ViewHolder {
        ImageView image;
        Handler handler;
    }
}