package com.subgarden.airbrush_project;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.subgarden.airbrush.AirBrush;

import java.util.ArrayList;

public class SimpleActivity extends AppCompatActivity {

    public final static String TAG = SimpleActivity.class.getSimpleName();

    protected int halfOfScreen;
    protected int tinyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        halfOfScreen = getResources().getDisplayMetrics().widthPixels / 2;
        tinyImage = 5;

        GridViewAdapter gridAdapter = new GridViewAdapter(this, R.layout.grid_layout_item, getData());
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(gridAdapter);
        gridView.setColumnWidth(halfOfScreen);

        gridView.setNumColumns(2);

    }

    // Prepare some dummy data for gridview
    private ArrayList<Item> getData() {
        final ArrayList<Item> imageItems = new ArrayList<>();

        // Full size images
        Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test1, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test2, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test3, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test4, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test5, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test6, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));

        // Tiny blurred images. Note that the gradient will be based on the blurred image, and thus look more monochrome
        final int radius = 4;
        final int scale = 2;

        Bitmap bitmapTiny = decodeSampledBitmapFromResource(getResources(), R.drawable.test1, tinyImage, tinyImage);
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test1, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.fastblur(bitmapTiny, scale, radius)));

        bitmapTiny = decodeSampledBitmapFromResource(getResources(), R.drawable.test2, tinyImage, tinyImage);
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test2, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.fastblur(bitmapTiny, scale, radius)));

        bitmapTiny = decodeSampledBitmapFromResource(getResources(), R.drawable.test3, tinyImage, tinyImage);
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test3, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.fastblur(bitmapTiny, scale, radius)));

        bitmapTiny = decodeSampledBitmapFromResource(getResources(), R.drawable.test4, tinyImage, tinyImage);
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test4, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.fastblur(bitmapTiny, scale, radius)));

        bitmapTiny = decodeSampledBitmapFromResource(getResources(), R.drawable.test5, tinyImage, tinyImage);
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test5, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.fastblur(bitmapTiny, scale, radius)));

        bitmapTiny = decodeSampledBitmapFromResource(getResources(), R.drawable.test6, tinyImage, tinyImage);
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test6, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.fastblur(bitmapTiny, scale, radius)));

        // More full size images
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test7, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test8, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test9, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test10, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test11, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));
        bitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test12, halfOfScreen, halfOfScreen);
        imageItems.add(new Item(bitmap, AirBrush.getPalette(bitmap)));

        long totalSize = 0;
        for (int i = 0; i < imageItems.size(); i++) {
            int height = imageItems.get(i).getImage().getHeight();
            int width = imageItems.get(i).getImage().getWidth();
            long bytes = width * height * 4;
            long kiloBytes = bytes / 1024 ;
            long megaBytes = bytes / 1024 / 1024 ;
            Log.d(TAG, "Dimensions: " + width + "x" + height + " Size: " + bytes + "B " + kiloBytes + "KB " + megaBytes + "MB");
            totalSize += bytes;
        }

        Log.d(TAG, "Total size of bitmaps in memory: " + (totalSize / 1024 / 1024));
        return imageItems;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inScaled = false;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Log.d(TAG, "inSampleSize = " + options.inSampleSize + " " + options.outHeight + "x" + options.outWidth);
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                   && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
