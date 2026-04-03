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

# Mediation SDK rules - Unity
-dontwarn com.unity3d.ads.**
-dontwarn com.unity3d.services.**

# Mediation SDK rules - AppLovin
-dontwarn com.applovin.**
-keep class com.applovin.** { *; }
-keep class com.google.android.gms.ads.mediation.** { *; }

# Mediation SDK rules - ironSource
-dontwarn com.ironsource.**
-keep class com.ironsource.** { *; }

# Mediation SDK rules - Liftoff (Vungle)
-dontwarn com.vungle.**
-keep class com.vungle.** { *; }

# Mediation SDK rules - InMobi
-dontwarn com.inmobi.**
-keep class com.inmobi.** { *; }

# Mediation SDK rules - Mintegral
-dontwarn com.mbridge.**
-keep class com.mbridge.** { *; }