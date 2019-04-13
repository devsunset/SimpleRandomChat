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

#빌드 후 mapping seed usage cofing 파일을 만들어주는 옵션
-printmapping map.txt
-printseeds seed.txt
-printusage usage.txt
-printconfiguration config.txt

#소스 파일의 라인을 섞지 않는 옵션 (이거 안해주면 나중에 stacktrace보고 어느 line에서 오류가 난 것인지 확인 불가)
-keepattributes SourceFile,LineNumberTable

#소스 파일 변수 명 바꾸는 옵션
-renamesourcefileattribute SourceFile

#보통 라이브러리는 딱히 난독화 할 필요없을 때 이렇게 적어준다.
#-keep class 라이브러리패키지명.** { *; }

#워닝뜨는거 무시할때
-ignorewarnings
#지정해서 워닝 무시할 때
#-dontwarn 패키지명.**

#아래의 3가지 것들은 default 요소들이지만 중요한 option이라 설명한다.
#-dontoptimize #없애면 난독화 X
#-dontobfuscate #없애면 최적화 X
#-keepresourcexmlattributenames manifest/** #없애면 manifest 난독화 X

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }