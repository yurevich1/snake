call clear

set JAVA_HOME="C:\Program Files\Java\jdk1.7.0
set JAVAC=%JAVA_HOME%\bin\javac"
set JAVA=%JAVA_HOME%\bin\java"
set KEYTOOL=%JAVA_HOME%\bin\keytool"
set SIGNER=%JAVA_HOME%\bin\jarsigner"

set ANDROID_HOME="z:\work\eclipse\_sourse__\sdk
set DEV_HOME=%CD%

set KEY_NAME=aSnake
set KEY_NAME_SIGNED=%KEY_NAME%
set KEY_NAME_UNSIGNED=%KEY_NAME%.unsigned
set STORE_PASS=science
set KEY_PASS=aSnake22
set ALIAS=aSnake22
set PROCLASSPATH=objPro
set ORIGCLASSPATH=obj
set LIBSCLASSPATH=libs

set AAPT_PATH=%ANDROID_HOME%\build-tools\18.0.0\aapt.exe"
set DX_PATH=%ANDROID_HOME%\build-tools\18.0.0\dx.bat"
set ANDROID_JAR=%ANDROID_HOME%\platforms\android-19\android.jar"
set ADB=%ANDROID_HOME%\platform-tools\adb.exe"

set PROGUARD_JAR=%ANDROID_HOME%\tools\proguard\lib\proguard.jar"
set PROGUARD_PRO=%DEV_HOME%\android.pro

set PACKAGE_PATH=ru\ps\habrsnake\
set PACKAGE=ru.ps.habrsnake
set MAIN_CLASS=.A

REM create folders
call md bin
call md libs
call md res

REM create R.java
REM call %AAPT_PATH% package -f -m -S %DEV_HOME%\res -J %DEV_HOME%\src -M %DEV_HOME%\AndroidManifest.xml -I %ANDROID_JAR%

REM compile, convert class->dex and create APK
call md %ORIGCLASSPATH%
call %JAVAC% -target 1.6 -source 1.6 -encoding utf8 -d %DEV_HOME%\%ORIGCLASSPATH% -cp %ANDROID_JAR% -sourcepath %DEV_HOME%\src %DEV_HOME%\src\%PACKAGE_PATH%\*.java
call md %PROCLASSPATH%
call %JAVA% -jar %PROGUARD_JAR% @%PROGUARD_PRO% -libraryjars %ANDROID_JAR% -injars %ORIGCLASSPATH% -injars %LIBSCLASSPATH% -outjars %PROCLASSPATH%\classes-processed.jar
call %DX_PATH% --dex --output=%DEV_HOME%\bin\classes.dex %DEV_HOME%\%PROCLASSPATH%
call %AAPT_PATH% package -f -M %DEV_HOME%\AndroidManifest.xml -S %DEV_HOME%\res -I %ANDROID_JAR% -F %DEV_HOME%\bin\%KEY_NAME_UNSIGNED%.apk %DEV_HOME%\bin

REM create key and signed APK
REM call %KEYTOOL% -genkey -validity 10000 -dname "CN=AndroidDebug, O=Android, C=US" -keystore %DEV_HOME%\%KEY_NAME%.keystore -storepass %STORE_PASS% -keypass %KEY_PASS% -alias %ALIAS% -keyalg RSA -keysize 2048
call %SIGNER% -sigalg SHA1withRSA -digestalg SHA1 -keystore %DEV_HOME%\%KEY_NAME%.keystore -storepass %STORE_PASS% -keypass %KEY_PASS% -signedjar %DEV_HOME%\bin\%KEY_NAME_SIGNED%.apk %DEV_HOME%\bin\%KEY_NAME_UNSIGNED%.apk %ALIAS%

REM reinstall and start APK on device
call %ADB% uninstall %PACKAGE%
call %ADB% install %DEV_HOME%\bin\%KEY_NAME_SIGNED%.apk
call %ADB% shell am start -n %PACKAGE%/%MAIN_CLASS%
pause

