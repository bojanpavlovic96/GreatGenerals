#!/bin/bash

# used by ordered build

build() {
	
	cd $1
	
	output=$(mvn clean install | grep "BUILD SUCCESS")
	
	if [ -z "$output" ]
	then 
		echo "0"
	else
		echo "1"
	fi
	
}

if [ $# -lt 1 ]
then 
	echo "ERROR: Missing projects"
else
	
	for project in $@
	do
		echo "BUILDING: " $project
		output=$(build $project)
		
		if [ $output == "1" ]
		then 
			
			echo "BUILD SUCCESS: " $project
			echo ""
			
		else
		
			echo "BUILD FAILURE: " $project 
			echo ""
			exit 1
			
		fi
		
	done

fi

