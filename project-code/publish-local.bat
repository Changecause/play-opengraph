@echo off
rem ### Variables
rem ##### Name of the project dir
set projectName=12OPG

echo Setting Java 6 and compile app...
set OLDPATH=%PATH%
set PATH=C:\Program Files (x86)\Java\jdk1.6.0_21\bin;C:\Program Files (x86)\Java\jre6\bin;C:\play
call play clean publish-local
set PATH=%OLDPATH%