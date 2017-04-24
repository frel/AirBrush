# AirBrush

![Logo](website/airbrush-logo.png)

_Be aware that this is still in alpha and applicable for breaking changes_

AirBrush is an Android Library for creating gradients that serve as placeholder images while images are loading.
Esthetically the gradients are very similar to a blurred version of the image they are representing. In fact that is the whole point of AirBrush.
To esthetically approximate a blurred version of an image, without sending a lot of data.

## Challenges with images
In short: They are big. 

You could get a nice blurred placeholder by receiving a small JPEG together with the full image URI. Potentially blurring it on the client.
Imagine having an endlessly scrolling list of images in the app. The server would then need to provide a JPEG placeholder for all the images. 
That would quickly increase the bandwidth usage of any servers running a large amount of users.

However, it might be adequate to provide an extremely small image of e.g. 5x5 pixels. Compressed as a JPEG it would take around 50 bytes.
Perhaps even smaller if the JPEG header could be omitted (i.e. added by the client). For more information see [Facebook's great blog post](https://code.facebook.com/posts/991252547593574/the-technology-behind-preview-photos).

(More on that in the future)

### The gradient
The gradient consist of four colors, one for each corner. They are represented using a Palette.

### The Palette
The color palette is intended to be passed along with other meta data, such as the image URI. 
This allows a client to display a gradient while it is loading a bigger image. 
On devices with slow network connections, this is especially noticeable. 

# Usage 


Simply call `Airbrush.getGradient(imageView, palette);` and use the Drawable as you see fit.

There is also a utility method for creating a Palette based on a bitmap. This can be very handy while developing.
`AirBrush.getPalette(bitmap);`

_Note that the minimum supported API level is 11._
## Transitioning 

By transitioning between the gradient and the actual image the effect is more noticeable.
Here is an example, however, I would recommend using an image loading library such as [Glide][1] instead.

Assuming that `item` is an instance holding the metadata.

```java
void onImageLoaded(Bitmap image) {
    Palette palette = item.getPalette();
    Drawable gradient = AirBrush.getGradient(imageView, palette);

    TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
        gradient,
        new BitmapDrawable(getResources(), image)
    });
    imageView.setImageDrawable(transitionDrawable);
    transitionDrawable.startTransition(300);
}
```

            

It should be noted that since AirBrush is using [ComposeShader] inside a ComposeShader, the view layer has to be rendered in software. AirBrush does this for you by calling
`ViewCompat.setLayerType(view, View.LAYER_TYPE_SOFTWARE, null);` See [Unsupported Drawing Operations][3].


Download
--------

```groovy
dependencies {
  compile 'com.subgarden.android:airbrush:0.5.1'
}
```

License
-------

    Copyright 2017 Fredrik Haugen Larsen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/bumptech/glide
[ComposeShader]: https://developer.android.com/reference/android/graphics/ComposeShader.html
[3]: https://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
