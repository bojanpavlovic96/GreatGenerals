import jsonpickle
import sys
from pika.credentials import PlainCredentials
import pika

BROKER_ADDRESS = "localhost"
BROKER_PORT = 5672
BROKER_V_HOST = 'gg_host'
BROKER_USER = "gg_user"
BROKER_PWD = "gg_password"


NEW_GAME_TOPIC = "new-game-topic"
NEW_GAME_ROUTING_KEY = "new-game-route"

MODEL_EVENT_TOPIC = "model-event-topic"
MODEL_EVENT_ROUTING_KEY = "model-event-filter"

SERVER_COMMAND_TOPIC = "server-command-topic"
SERVER_COMMAND_ROUTING_KEY = "server-command-filter"


class Point2D():
    def __init__(self, x, y):
        self.x = x
        self.y = y


class MoveModelEvent():
    def __init__(self,
                 event_name: str,
                 player_name: str,
                 source_field: Point2D,
                 dest_field: Point2D):

        self.eventName = event_name
        self.playerName = player_name
        self.sourceField = source_field
        self.destinationField = dest_field


creds = PlainCredentials(
    username=BROKER_USER,
    password=BROKER_PWD)
parameters = pika.ConnectionParameters(
    host=BROKER_ADDRESS,
    port=BROKER_PORT,
    virtual_host=BROKER_V_HOST,
    credentials=creds)

connection = pika.BlockingConnection(parameters)

channel = connection.channel()
channel.exchange_declare(exchange=NEW_GAME_TOPIC,
                         exchange_type='topic',
                         durable=False,
                         auto_delete=True,
                         arguments=None)


data = MoveModelEvent(
    "move-model-event",
    "unknown_name",
    Point2D(1, 1),
    Point2D(2, 2))

str_data = jsonpickle.encode(data, unpicklable=False)

channel.basic_publish(exchange=NEW_GAME_TOPIC,
                      routing_key=NEW_GAME_ROUTING_KEY,
                      body=str_data)

print("Message sent ... ")

channel.close()
connection.close()
