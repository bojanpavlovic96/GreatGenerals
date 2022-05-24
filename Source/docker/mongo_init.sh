# todo extract username, password ... in to the variables to that they can be 
# found and replaced without digging trough the query 
mongo --eval 'db.createUser({user:"gg_user", pwd:"gg_password", roles:[{role:"readWrite", db:"gg_games"}] })'