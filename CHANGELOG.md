# AirBrush changelog

## Version 0.6.1

A few enhancements: 
- TinyThumbDecoder now supports a different default base64 decode flag.
- TinyThumb can now override the default base64 decode flag.
- Removed bitmapProvider from TinyThumbDecoder. A custom decoder class should be used instead.
- Added @JvmOverload to better support default arguments from Java
- Added more documentation
- Upgraded to latest kotlin and support library versions

## Version 0.6.0
Big rewrite for optimization.
- Using Glide 4 and custom loaders to for thumbnails.
- Using RenderScript to generate gradients.
- Renamed Palette to GradientPalette.
- Introducing TinyThumb which takes a base64 encoded JPEG. It's decoded and blurred at runtime.
- The library GlideModule handles registration of decoders/loaders, but thus can be customized in the App's GlideModule. See sample app for details.
- Updated sample app to reflect changes.

To generate a Bitmap from a GradientPalette:

```
AirBrush(context).getGradient(gradientPalette, width, height)
```

Note that it must be done on a worker thread.

### Image Loading

For loading TinyThumb or GradientPalette with Glide, load it directly.
For TinyThumb:

```
    val tinyThumb = getTinyThumb()
    requestManager
                .load(imageUri)
                .thumbnail(requestManager.load(tinyThumb))
                .into(imageView)
```

For GradientPalette:
```
    val gradientPalette = getGradientPalette()
    requestManager
                .load(imageUri)
                .thumbnail(requestManager.load(gradientPalette))
                .into(imageView)
```

### Utility methods

```
AirBrush.blur(context, bitmap, scale, radius)

AirBrush.getPalette(bitmap);
```


## Version 0.5.1 

First release available through maven.

To get a gradient drawable use 
```
Airbrush.getGradient(imageView, palette);
```

There is also a utility method for creating a Palette based on a bitmap. This can be very handy while developing. 

```
AirBrush.getPalette(bitmap);
```

_Note:_ Since AirBrush is using a ComposeShader inside a ComposeShader, the view layer has to be rendered in software. 
AirBrush does this for you by calling `ViewCompat.setLayerType(view, View.LAYER_TYPE_SOFTWARE, null);`



