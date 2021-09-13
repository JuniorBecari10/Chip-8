@echo off

cd src

javac -d ../bin chip/main/Main.java
cd ../bin
java chip.main.Main "C:/Users/Juninho/Documents/Projetos e Coisas/Chip-8/Java/roms/BLINKY"

exit
