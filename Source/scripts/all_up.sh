#!/bin/bash

docker start gg-broker gg-users-db gg-game-db

echo "sleeping ... "
sleep 3

docker start gg-login-server
docker start gg-game-server
