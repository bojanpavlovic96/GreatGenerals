#!/bin/bash

echo "Clearing games ... "
docker exec -i gg-game-db mongo localhost/gg_games --eval 'db.Games.remove({})'

echo "Clearing messages ... " 
docker exec -i gg-game-db mongo localhost/gg_games --eval 'db.MsgContainers.remove({})'

echo "Exiting ... "
