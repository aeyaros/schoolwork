#!/bin/bash
#Andrew Yaros
#CS 360 PA 1 Questions 3, 4
printf "\nAndrew Yaros CS360 PA1 bash file\n"
cd 3
make clean
make
cd ../4
make clean
make
cd ..
printf "\nRead-me file\n"
cat Readme.txt
cd 3
printf "\nQuestion 3\n"
java Main
cd ../4
printf "\nQuestion 4\n"
java Main
cd ..
