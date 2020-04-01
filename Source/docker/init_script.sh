#!/bin/bash

rabbitmqctl wait --timeout 30 1

rabbitmqctl add_user gg_user gg_password
