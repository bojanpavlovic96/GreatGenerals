# if curlbian image is not recognized (doesn't exists) 
# run $ docker build --tag curlbian --file curlbian.Dockerfile . 
# clear_pool script should work now 

docker run -i --rm --network gg_network curlbian curl --connect-timeout 3 gg-game-server:9091/manage/all
