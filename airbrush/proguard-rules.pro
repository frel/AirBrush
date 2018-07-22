# We don't need obfuscation.
-dontobfuscate

-dontwarn com.subgarden.android.airbrush.**

# Preserve all annotations.
-keepattributes *Annotation*

# Preserve all public classes, and their public and protected fields and
# methods.
-keep public class * {
    public protected *;
}

# Preserve all .class method names.
-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

# RenderScript
-keep class android.support.v8.renderscript.** { *; }

# Glide
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

# Palette
-keep class android.support.v7.graphics.** { *; }
