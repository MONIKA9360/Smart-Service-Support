@REM ----------------------------------------------------------------------------
@REM Maven Wrapper Batch Script for Windows
@REM ----------------------------------------------------------------------------
@IF "%DEBUG%" == "true" @ECHO ON

@SETLOCAL

@SET MAVEN_CMD=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin\apache-maven-3.9.6\bin\mvn.cmd

@IF EXIST "%MAVEN_CMD%" (
  @"%MAVEN_CMD%" %*
  @EXIT /B %ERRORLEVEL%
)

@REM Fallback to standard mvn in PATH if available
@WHERE mvn >nul 2>nul
@IF %ERRORLEVEL% EQU 0 (
  @mvn %*
  @EXIT /B %ERRORLEVEL%
)

@ECHO Apache Maven not found at %MAVEN_CMD% and 'mvn' is not in PATH.
@EXIT /B 1
