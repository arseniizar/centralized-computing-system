@echo off
REM Default server IP and port
SET SERVER_IP=127.0.0.1
SET PORT=12345

REM Check if arguments were passed
IF NOT "%~1"=="" SET SERVER_IP=%~1
IF NOT "%~2"=="" (
    REM Validate port is a number
    FOR /F "delims=0123456789" %%A IN ("%~2") DO (
        echo Error: Port must be a valid number.
        EXIT /B 1
    )
    SET PORT=%~2
)

REM Check if ClientSimulator.jar exists
IF NOT EXIST ClientSimulator.jar (
    echo Compiling ClientSimulator...
    javac ClientSimulator.java
    IF ERRORLEVEL 1 (
        echo Error: Compilation failed.
        EXIT /B 1
    )
    echo Main-Class: ClientSimulator > manifest.txt
    jar cfm ClientSimulator.jar manifest.txt ClientSimulator.class
    IF ERRORLEVEL 1 (
        echo Error: Failed to create ClientSimulator.jar.
        EXIT /B 1
    )
    del manifest.txt
    echo ClientSimulator.jar created successfully.
)

REM Run the ClientSimulator
java -jar ClientSimulator.jar %SERVER_IP% %PORT%

REM Cleanup: Remove .class and .jar files after execution
echo Cleaning up...
del /Q ClientSimulator.class
del /Q ClientSimulator.jar
echo Cleanup complete.
