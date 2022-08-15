#!/bin/bash

echo "To clean project(s) take a look at the NOTE text file"
echo "This script has not been updated for a long time ... "
echo "Press ENTER to proceed with the old script ... "
echo "Or ctrl+c to abort ... "
read enter_input

./single_build.sh ../root ../client-model ../client-view ../client-controller ../communication ../server ../client-application
