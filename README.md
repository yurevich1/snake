usage:
DO NOT FORGET TO SETUP ALL VARIABLES INSIDE BATCH FILES! SUCH AS JAVA_PATH, etc.

1. In order to create aSnake.keystore use sign.bat
2. Compile project comp.bat


DO NOT FORGET TO UNCOMMENT R.java generation line if you want to use this code in other projects:
 that is, change
REM call %AAPT_PATH% package -f -m -S %DEV_HOME%\res -J %DEV_HOME%\src -M %DEV_HOME%\AndroidManifest.xml -I %ANDROID_JAR%
 to 
call %AAPT_PATH% package -f -m -S %DEV_HOME%\res -J %DEV_HOME%\src -M %DEV_HOME%\AndroidManifest.xml -I %ANDROID_JAR%

