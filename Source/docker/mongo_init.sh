# todo extract username, password ... in to the variables to that they can be 
# found and replaced without digging trough the query 
mongo localhost:27017/gg_games --eval 'db.createUser({user:"gg_user", pwd:"gg_password", roles:[{role:"readWrite", db:"gg_games"}] })'