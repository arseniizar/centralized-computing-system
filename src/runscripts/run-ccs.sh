#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: ./run-ccs.sh <port>"
    exit 1
fi

PORT=$1
JAR_NAME="CCS.jar"

if [ ! -f "CCS.java" ]; then
    echo "Error: CCS.java not found in the current directory."
    exit 1
fi

echo "Compiling CCS.java..."
javac -d . CCS.java
if [ $? -ne 0 ]; then
    echo "Error: Compilation failed."
    exit 1
fi

echo "Creating $JAR_NAME..."
jar cfe CCS.jar CCS CCS*.class
if [ $? -ne 0 ]; then
    echo "Error: Failed to create JAR file."
    exit 1
fi

echo "Cleaning up..."
rm -rf CCS*.class

echo "Starting CCS application on port $PORT..."
java -jar $JAR_NAME "$PORT"
