set JAVA_HOME="C:\Program Files\Java\jdk1.7.0
set KEYTOOL=%JAVA_HOME%\bin\keytool"
set DEV_HOME=%CD%

set KEY_NAME=aSnake
set STORE_PASS=science
set KEY_PASS=aSnake22
set ALIAS=aSnake22
REM create key and signed APK
call %KEYTOOL% -genkey -validity 10000 -dname "CN=AndroidDebug, O=Android, C=US" -keystore %DEV_HOME%\%KEY_NAME%.keystore -storepass %STORE_PASS% -keypass %KEY_PASS% -alias %ALIAS% -keyalg RSA -keysize 2048