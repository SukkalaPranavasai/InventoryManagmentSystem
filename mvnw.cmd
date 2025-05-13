@echo off 
setlocal 
set DIRNAME=%~dp0 
if "%DIRNAME%" == "" set DIRNAME=. 
set APP_BASE_NAME=%~n0 
set APP_HOME=%DIRNAME% 
set JAVA_EXE=java.exe 
set WRAPPER_JAR=%APP_HOME%\.mvn\wrapper\maven-wrapper.jar 
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain 
%JAVA_EXE% -jar "%WRAPPER_JAR%" %* 
