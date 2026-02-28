@REM
@REM #############################################################################
@REM #
@REM # Gradle build script for Windows
@REM #
@REM #############################################################################
@REM

@IF EXIST "%~dp0\gradle\wrapper\gradle-wrapper.jar" GOTO init

@echo Error: Could not find or access the Gradle wrapper JAR file.
@echo Please ensure that the file exists at "%~dp0\gradle\wrapper\gradle-wrapper.jar"
@pause
@EXIT /B 1

:init
@SETLOCAL

@REM Set the Java command to use
@IF DEFINED JAVA_HOME GOTO findJavaFromJavaHome
@SET JAVACMD=java
@GOTO checkJava

:findJavaFromJavaHome
@SET JAVACMD="%JAVA_HOME%\bin\java.exe"
@IF EXIST "%JAVACMD%" GOTO checkJava
@SET JAVACMD="%JAVA_HOME%\jre\bin\java.exe"
@IF EXIST "%JAVACMD%" GOTO checkJava
@SET JAVACMD=java

:checkJava
@%JAVACMD% -version >NUL 2>&1
@IF %ERRORLEVEL% NEQ 0 GOTO noJava
@GOTO runGradle

:noJava
@echo Error: JAVA_HOME is not defined correctly.
@echo   We cannot execute java
@pause
@EXIT /B 1

:runGradle
@SET CLASSPATH=%~dp0\gradle\wrapper\gradle-wrapper.jar
@SET DEFAULT_JVM_OPTS=-Xmx64m -Xms64m
@SET APP_HOME=%~dp0
@SET APP_NAME="Gradle"
@SET APP_BASE_NAME=%~nx0
@REM Add default JVM options here. You may also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
@SET DEFAULT_JVM_OPTS=%DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS%
@"%JAVACMD%" %DEFAULT_JVM_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
@IF %ERRORLEVEL% NEQ 0 GOTO fail
@GOTO end

:fail
@REM If the script fails, it will return a non-zero exit code. This can be useful for CI/CD pipelines.
@EXIT /B %ERRORLEVEL%

:end
@ENDLOCAL
