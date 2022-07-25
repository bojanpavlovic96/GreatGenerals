username='gg_user'
password='gg_password'
# host='localhost'
host='%' 
# wildcard matching all hosts
# you can create accounts with the same name but different hostname 
# hostname is the address of host from which user is gonna access this db 
# https://dev.mysql.com/doc/refman/5.7/en/account-names.html
database='gg_players'

query="FLUSH PRIVILEGES"
echo "Flushing priliveges: $query"
mysql -u root -proot -e "$query"
echo 

query="CREATE USER \"$username\"@\"$host\" IDENTIFIED BY \"$password\""
echo "Create user: $query"
mysql -u root -proot -e "$query"
echo 

query="CREATE DATABASE $database"
echo "Create database: $query"
mysql -u root -proot -e "$query"
echo 

query="GRANT ALL ON $database.* TO $username"
echo "Grant permissions: $query"
# mysql -u root -proot -e "GRANT ALL ON gg_users.* TO gg_user@localhost"
mysql -u root -proot -e "$query"
echo 

# no it is not an error, root password has to be together with the -p arg (-proot)

