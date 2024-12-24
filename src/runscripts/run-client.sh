#!/bin/bash

SERVER_IP="127.0.0.1"
PORT="12345"

if [ "$1" != "" ]; then
  SERVER_IP=$1
fi
if [ "$2" != "" ]; then
  PORT=$2
fi

if [ ! -f "ClientSimulator.jar" ]; then
  echo "Compiling ClientSimulator..."

  # Compile ClientSimulator.java
  javac ClientSimulator.java
  if [ $? -ne 0 ]; then
    echo "Error: Compilation failed."
    exit 1
  fi

  echo "Main-Class: ClientSimulator" > manifest.txt
  jar cfm ClientSimulator.jar manifest.txt ClientSimulator.class
  if [ $? -ne 0 ]; then
    echo "Error: Failed to create ClientSimulator.jar."
    exit 1
  fi
  rm manifest.txt
  echo "ClientSimulator.jar created successfully."
fi

java -jar ClientSimulator.jar "$SERVER_IP" "$PORT"
