#!/bin/bash

echo "To clean project(s) take a look at the NOTE text file"
echo "This script has not been updated for a long time ... "
echo "Press ENTER to proceed with the old script ... "
echo "Or ctrl+c to abort ... "
read enter_input

echo "Going to run GreatGenerals"
cd ../client-application 
java -jar ./target/client-application-0.0.1-SNAPSHOT-jar-with-dependencies.jar
# mvn clean javafx:run
