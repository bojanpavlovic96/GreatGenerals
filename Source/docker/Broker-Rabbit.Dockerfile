# don't forget to update version 
# pick management version  if you want to be able to access web management ui
# and more important to be ale to create users defined in definitions.json file
FROM rabbitmq:3.8.3-management

ADD ./docker/rabbit_definitions.json /etc/rabbitmq/definitions.json

RUN chown rabbitmq:rabbitmq /etc/rabbitmq/definitions.json

# this base image doesn't support this way of initializing container
# sugested way is by useing config (in my case definition.json) files
# ADD ./docker/rabbit_init.sh /docker-entrypoint-initdb.d/

# entrypoint will be inherited from parent image
# CMD ["rabbitmq-server"]
