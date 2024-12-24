@echo off

IF "%~1"=="" (
    echo Usage: run-ccs.bat ^<port^>
    exit /b 1
)

SET PORT=%~1
SET JAR_NAME=CCS.jar

IF NOT EXIST "CCS.java" (
    echo Error: CCS.java not found in the current directory.
    exit /b 1
)

echo Compiling CCS.java...
javac -d . CCS.java
IF ERRORLEVEL 1 (
    echo Error: Compilation failed.
    exit /b 1
)

echo Creating %JAR_NAME%...
jar cfe CCS.jar CCS CCS*.class
IF ERRORLEVEL 1 (
    echo Error: Failed to create JAR file.
    exit /b 1
)

echo Cleaning up...
del CCS*.class

echo Starting CCS application on port %PORT%...
java -jar %JAR_NAME% %PORT%
