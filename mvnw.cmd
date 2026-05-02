@REM Apache Maven Wrapper Script (Windows)
@REM

@if "%DEBUG%" == "" @echo off
@setlocal enableextensions enabledelayedexpansion

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.

set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set MAVEN_HOME=%APP_HOME%\.mvn

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo Error: JAVA_HOME is not set properly. Please set JAVA_HOME to the JDK directory.
    exit /b 1
)

"%JAVA_HOME%\bin\java.exe" ^
  -classpath "%MAVEN_HOME%\wrapper\maven-wrapper.jar" ^
  "-Dmaven.multiModuleProjectDirectory=%APP_HOME%" ^
  "-Dmaven.home=%MAVEN_HOME%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*

:end
@endlocal & exit /b %ERRORLEVEL%
