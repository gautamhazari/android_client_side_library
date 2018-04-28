-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
 native <methods>;
}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }


-dontwarn android.support.**
-dontwarn com.google.ads.**

-keep class com.gsma.** { *; }
-keep interface com.gsma.** { *; }
-keepattributes Annotation
-keepclasseswithmembers class com.fasterxml.jackson.** { *; }
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn org.apache.commons.codec.binary.Base64
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.bouncycastle.jce.ECNamedCurveTable
-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn org.apache.http.entity.StringEntity
