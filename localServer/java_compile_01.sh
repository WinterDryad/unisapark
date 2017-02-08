#!/bin/bash

javac -cp ./classes -d classes src/LocalUpdaterSim.java
javac -cp ./classes -d classes src/RemoteUpdater.java
javac -cp ./classes -d classes src/Main.java

cd classes

java localServer.Main