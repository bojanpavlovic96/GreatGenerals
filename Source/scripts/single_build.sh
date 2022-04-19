#!/bin/bash

echo "To clean project(s) take a look at the NOTE text file"
echo "This script has not been updated for a long time ... "
echo "Press ENTER to proceed with the old script ... "
echo "Or ctrl+c to abort ... "
read enter_input

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
		build_start_time=`date +%s`
		printf "Building started: %s" $project
		output=$(build $project)
		
		if [ $output == "1" ]
		then 
			printf " -> -SUCCESS- \n"
		else
			printf " -> -FAILURE- \n"
			failure_moment=`date +%s`
			failure_time=$((failure_moment-build_start_time))
			printf "Build fail after: %d seconds\n" $failure_time
			 
			exit 1
		fi

		build_end_time=`date +%s`
		build_time=$((build_end_time-build_start_time))
		printf "Build done after: %d seconds\n" $build_time
		echo ""
	done

fi

