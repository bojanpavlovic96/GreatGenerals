#!/bin/bash

addr="127.0.0.1"
username="gg_user"
password="gg_password"
database="gg_players"

if [ $# != 1 ]
then 
	echo "Please provide query as the first (and only) argument ... "
	exit 1
fi

mysql -u $username -p$password --host "$addr" --database "$database" --execute "$1"

