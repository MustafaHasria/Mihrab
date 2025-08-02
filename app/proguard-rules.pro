# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Add this global rule
-keepattributes Signature

# Keep MTBeaconPlus.jar classes to avoid stack map table issues
-keep class com.mtbeacon.** { *; }
-dontwarn com.mtbeacon.**

# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep YouTube API classes
-keep class com.google.android.youtube.** { *; }
-dontwarn com.google.android.youtube.**

# Keep Glide classes
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Keep Material Design classes
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# Keep AndroidX classes
-keep class androidx.** { *; }
-dontwarn androidx.**

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models.
# Modify this rule to fit the structure of your app.
#-keepclassmembers class com.yourcompany.models.** {
#  *;
#}