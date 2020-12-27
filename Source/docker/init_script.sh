#!/bin/bash

# don't know what is this 
rabbitmqctl wait --timeout 30 1

rabbitmqctl add_user gg_user gg_password

rabbitmqctl add_vhost gg_host

rabbitmqctl add_permissions gg_user --vhost gg_host ".*" ".*" ".*"

