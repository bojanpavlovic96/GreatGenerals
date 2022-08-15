#!/bin/bash

docker exec gg-broker rabbitmqctl add_user guest guest
docker exec gg-broker rabbitmqctl set_user_tags guest administrator
docker exec gg-broker rabbitmqctl set_permissions -p gg_host guest ".*" ".*" ".*"

echo "Created user: guest, pwd: guest, with admin privileges ... "
