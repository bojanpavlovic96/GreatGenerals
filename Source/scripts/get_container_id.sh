#!/bin/bash

if [ $# != 1 ]
then 
	echo "Please provide container name as the first (and only) argument."

	exit 1
fi 

docker inspect "$1" --format "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}"
