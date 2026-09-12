@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.
@REM
@REM Maven Wrapper batch script for Windows
@REM https://maven.apache.org/wrapper/
@REM

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)

@SET MAVEN_PROJECTBASEDIR=%~dp0
@IF NOT "%MAVEN_BASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%

@SET MVNW_REPODIR=%USERPROFILE%\.m2\wrapper\dists

@powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0.mvn\wrapper\MavenWrapperDownloader.ps1" %*
@IF ERRORLEVEL 1 GOTO END

@FOR /F "tokens=*" %%G IN ('powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0.mvn\wrapper\MavenWrapperDownloader.ps1" --get-mvn-path') DO SET MAVEN_CMD=%%G

@IF NOT EXIST "%MAVEN_CMD%" (
  @echo Could not download Maven. Please install Maven manually.
  @GOTO END
)

@"%MAVEN_CMD%" %*

:END
