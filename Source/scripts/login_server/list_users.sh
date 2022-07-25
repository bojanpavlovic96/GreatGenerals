#!/bin/bash

if [ $# != 1 ]
then 
	echo "Please provide container name as the first (and only) argument."

	exit 1
fi 

query="SELECT * FROM player"

# can be hardcoded to 127.0.0.1 (localhost won't work, has to be 127.0.0.1)
db_ip=$(docker inspect "$1" --format "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}")
mysql --user gg_user -pgg_password \
	--host $db_ip --database gg_players \
	--execute "$query"
