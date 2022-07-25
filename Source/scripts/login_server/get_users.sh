#!/bin/bash

if [ $# != 1 ]
then 
	echo "Please provide container name as the first (and only) argument."

	exit 1
fi 

query="SELECT * FROM gg"

db_ip=$(docker inspect "$1" --format "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}")
mysql --user gg_user --password gg_password \ 
	--host $db_ip --database gg_players \
	--execute "$query"
