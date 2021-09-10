@echo off

cd src

javac -d ../bin chip/main/Main.java
cd ../bin
java chip.main.Main

exit
