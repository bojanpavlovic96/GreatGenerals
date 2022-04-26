import jsonpickle
import sys
from pika.credentials import PlainCredentials
import pika

BROKER_ADDRESS = "localhost"
BROKER_PORT = 5672
BROKER_V_HOST = 'gg_host'
BROKER_USER = "gg_user"
BROKER_PWD = "gg_password"

NEW_GAME_TOPIC = "gg_new_game"
NEW_GAME_ROUTING_KEY = "new_game."

MODEL_EVENT_TOPIC = "gg_model_event"
MODEL_EVENT_ROUTING_KEY = "model_event."

SERVER_COMMAND_TOPIC = "gg_server_command"
SERVER_COMMAND_ROUTING_KEY = "command."

TARGET_TOPIC = NEW_GAME_TOPIC
TARGET_ROUTE = NEW_GAME_ROUTING_KEY + "test"


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


class NewGameRequest():
    def __init__(self,
                 roomName: str,
                 players: list):

        self.roomName = roomName
        self.players = players


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
channel.exchange_declare(exchange=TARGET_TOPIC,
                         exchange_type='topic',
                         durable=False,
                         auto_delete=True,
                         arguments=None)


model_data = MoveModelEvent(
    "move-model-event",
    "unknown_name",
    Point2D(1, 1),
    Point2D(2, 2))

new_game_data = NewGameRequest("some_room_name", ["player1", "player2"])

str_model_data = jsonpickle.encode(model_data, unpicklable=False)
str_new_game_data = jsonpickle.encode(new_game_data, unpicklable=False)

TARGET_STR_DATA = str_new_game_data

channel.basic_publish(exchange=TARGET_TOPIC,
                      routing_key=TARGET_ROUTE,
                      body=TARGET_STR_DATA)

print("Topic: " + TARGET_TOPIC)
print("Route: " + TARGET_ROUTE)
print("Data: " + TARGET_STR_DATA)

channel.close()
connection.close()
