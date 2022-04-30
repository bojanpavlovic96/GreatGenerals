FROM mongo:latest

# all scripts with extension .sh or .js found in /docker-entrypoint-initdb.d/
# will be executed in alphabetical order on startup (after the mongo server is started)

ADD mongo_init.sh /docker-entrypoint-initdb.d/

# entrypoint will be inherited from parent image