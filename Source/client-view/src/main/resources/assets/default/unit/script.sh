#!/bin/bash

for single_file in * 
	do
		
		full_name=$(basename -- "$single_file")	
		extension="${full_name##*.}"
		short_name="${full_name%.*}"
		non_numbered="${short_name::-1}"
		
		if [ $extension == "png" ]
		then 
			echo "Working with: " $full_name
			
			mv $full_name "$non_numbered-1.$extension"
			
		fi 		
		
		
	done
