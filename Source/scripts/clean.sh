#!/bin/bash

clean() {

	cd $1
	output=$(mvn clean | grep -i "success")
	
	if [ -z "$output" ]
	then 
		echo "0"
	else
		echo "1"
	fi

}

# targets=()

if [ $# -ge "1" ]
then

	echo "Clean target provided, going to clean: " $1
	targets=("../client-nestovib")

else
	echo "Cleaning all projects ... "
	targets=("../root", "../client-application" "../client-view" "../client-model" "../client-controller")
fi

echo "" # new line

for single_target in ${targets[@]}
do

	printf "Cleaning: " $single_target
	clean_output=$(clean $single_target)

	if [ $clean_output=="1" ]
	then
		printf " -> SUCCESS\n"
	else
		printf " -> FAIILURE\n"
	fi

done
