#!/bin/bash

if [ $# != 1 ]
then
        echo "Please provide username as the first (and only) argument."

        exit 1
fi


curl localhost:9001/replay/games/some 