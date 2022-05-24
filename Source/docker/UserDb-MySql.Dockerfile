FROM mysql 

# all scripts with extension .sh or .js found in /docker-entrypoint-initdb.d/
# will be executed in alphabetical order on startup (after the mysql server is started)

# required by mysql
# I guess this is not the best place to define env.var but ... 
# ENV MYSQL_USER root 
ENV MYSQL_ROOT_PASSWORD root

ADD ./docker/mysql_init.sh /docker-entrypoint-initdb.d/

# entrypoint will be inherited from parent image