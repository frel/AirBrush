# AirBrush changelog

## Version 0.6.0
Big rewrite for optimization.
- Hardware Acceleration now possible by using RenderScript to generate the gradients.
- Using Glide 4 and a custom loader to generate and cache the gradients.


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



