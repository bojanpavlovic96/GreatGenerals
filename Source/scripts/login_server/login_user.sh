#!/bin/bash

data_username="test_username"
data_password="passwd"

if [ $# == 2 ]
then
	echo "We got two arguments ... "
	data_username="$1"
	data_password="$2"
fi 

address='localhost'
port=9000
path='login'
# data='{"username":'$data_username',"password":'$data_password'}'
data="{\"username\":\"$data_username\",\"password\":\"$data_password\"}"

echo "Will use data: $data"

url="$address:$port/$path"

echo "url on: $url"

curl --data "$data" --header 'Content-type: application/json' "$url"

db_ip="127.0.0.1" # localhost won't work, mysql requires 127.0.0.1
players_db="gg_players"

echo ""
echo "Press enter to list users ... "
a=2
read a

query="SELECT * FROM player"

mysql --user gg_user -pgg_password \
	--host "$db_ip" --database "$players_db" \
	--execute "$query"
