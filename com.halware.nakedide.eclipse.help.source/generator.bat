@echo off

rem -----------------------------------------------------------------
rem
rem Driver script for the DOPUS docbook environment
rem
rem -----------------------------------------------------------------

setlocal

REM The DOPUS root directory
set DOPUS_HOME=%~dp0

set SYSTEM_HOME=%DOPUS_HOME%\system
set ANT_HOME=%SYSTEM_HOME%\ant

REM Check if we supply our own JRE
if EXIST "%SYSTEM_HOME%\jre\bin\java.exe" goto with_jre 

REM Check if JAVA_HOME is set otherwise
if not "%JAVA_HOME%" == "" goto javahome_set 
echo You don't use Dopus that comes with preinstalled Java.
echo You don't seem to have the variable JAVA_HOME set.
echo To use Dopus, either download the one that includes a JRE
echo or set JAVA_HOME to a preinstalled Java of version 1.4 or above.
echo Exiting now...
goto the_end


:with_jre
set JAVA_HOME=%SYSTEM_HOME%\jre

:javahome_set
pushd %ANT_HOME%\lib
FOR /F %%i IN ('dir /b *.jar ') DO @call "%ANT_HOME%\bin\lcp.bat" %%~fi
popd

set PARAM_LINE=-Dproject.input.document=%1 -Dproject.output.mode=%2

if not "%3" == "" set PARAM_LINE=%PARAM_LINE% -DENV_PARAM_3=%3
rem if not "%4" == "" set PARAM_LINE=%PARAM_LINE% -DENV_PARAM_4=%4

set ANT_TARGET=%2

if "%1%" == "" goto help
if not "%1" == "help" goto run_ant

:help
echo # Usage: generator.bat document-name target
echo # A special case is creating a book or article:
echo #   generator.bat document-name create book    or
echo #   generator.bat document-name create article
echo #
echo #
set ANT_TARGET=-projecthelp

:run_ant
REM to have ant being quiet, add -quiet before %ANT_TARGET% 
"%JAVA_HOME%\bin\java" -cp "%LOCALCLASSPATH%" %PARAM_LINE% org.apache.tools.ant.Main -buildfile "%DOPUS_HOME%\system\etc\build-main.xml" -logger org.apache.tools.ant.NoBannerLogger %ANT_TARGET%

:the_end

endlocal
