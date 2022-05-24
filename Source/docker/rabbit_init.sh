#!/bin/bash

# script is not used, users are created using different approach 
# it is kinda not suggested by the docs ... bu 

# don't know what is this 
# rabbitmqctl wait --timeout 30 1

# username="gg_user"
# password="gg_password"
# vhost="gg_host"

echo "Adding user ... "
rabbitmqctl add_user gg_user gg_password
echo 

echo "Adding vhost ... "
rabbitmqctl add_vhost gg_host
echo 

echo "Adding permissions ... "
rabbitmqctl set_permissions gg_user --vhost gg_host ".*" ".*" ".*"
echo 