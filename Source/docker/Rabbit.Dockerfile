# don't forget to update version 
# pick management version  if you want to be able to access web management ui
# and more important to be ale to create users defined in definitions.json file
FROM rabbitmq:3.8.3-management

ADD definitions.json /etc/rabbitmq/definitions.json

RUN chown rabbitmq:rabbitmq /etc/rabbitmq/definitions.json

# entrypoint will be inherited from parent image
# CMD ["rabbitmq-server"]
