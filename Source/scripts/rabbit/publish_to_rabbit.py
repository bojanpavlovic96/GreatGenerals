import jsonpickle
import sys
from pika.credentials import PlainCredentials
import pika

BROKER_ADDRESS = "localhost"
BROKER_PORT = 5672
BROKER_V_HOST = 'gg_host'
BROKER_USER = "gg_user"
BROKER_PWD = "gg_password"

NEW_GAME_TOPIC = "gg_rooms_req"
NEW_GAME_ROUTING_KEY = "new_game."

MODEL_EVENT_TOPIC = "gg_model_event"
MODEL_EVENT_ROUTING_KEY = "model_event."

SERVER_COMMAND_TOPIC = "gg_server_command"
SERVER_COMMAND_ROUTING_KEY = "command."

NEW_GAME_RESPONSE_TOPIC = "gg_rooms_res"
NEW_GAME_RESPONSE_ROUTE = "new_game."

TARGET_TOPIC = NEW_GAME_TOPIC
TARGET_ROUTE = NEW_GAME_ROUTING_KEY + "some_room.some_user"
#TARGET_TOPIC = NEW_GAME_RESPONSE_TOPIC
#TARGET_ROUTE = NEW_GAME_RESPONSE_ROUTE + "some_room.some_user"


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
                 password: str,
                 username: str):
        self.password = password
        self.type = "CreateRoomRequest"
        self.roomName = roomName
        self.player = username


class Message():
    def __init__(self, name: str, payload: str):
        self.name = name
        self.payload = payload


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

# invalid
model_data = MoveModelEvent(
    "move-model-event",
    "unknown_name",
    Point2D(1, 1),
    Point2D(2, 2))


new_game_data = NewGameRequest("some_room", "some_password", "some_user")

str_model_data = jsonpickle.encode(model_data, unpicklable=False)
str_new_game_data = jsonpickle.encode(new_game_data, unpicklable=False)

message_data = Message("CreateRoomRequest", str_new_game_data)
str_message_data = jsonpickle.encode(message_data, unpicklable=False)

TARGET_STR_DATA = str_message_data

print("Topic: " + TARGET_TOPIC)
print("Route: " + TARGET_ROUTE)
print("Data: " + TARGET_STR_DATA)

channel.basic_publish(exchange=TARGET_TOPIC,
                      routing_key=TARGET_ROUTE,
                      body=TARGET_STR_DATA)

print("SENT ... ")

channel.close()
connection.close()
