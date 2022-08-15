import signal
import jsonpickle
import sys
from pika.credentials import PlainCredentials
import pika

BROKER_ADDRESS = "localhost"
BROKER_PORT = 5672
BROKER_V_HOST = 'gg_host'
BROKER_USER = "gg_user"
BROKER_PWD = "gg_password"

MESSAGE_NAME = "JoinRoomRequest"

ROOM_NAME = "some_room"
ROOM_PASSWORD = "room_pwd_1"
ROOM_USER = "some_user_2"

PUBLISH_TOPIC = "gg_rooms_req"
PUBLISH_ROUTE = "join_game" + "." + ROOM_NAME + "." + ROOM_USER

RESPONSE_TOPIC = "gg_rooms_res"
RESPONSE_ROUTE = "room_response." + ROOM_NAME + ".#"


class JoinRoomMessage():
    def __init__(self,  player: str, roomName: str, password: str):
        self.type = MESSAGE_NAME
        self.roomName = roomName
        self.player = player
        self.password = password


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
channel.exchange_declare(exchange=PUBLISH_TOPIC,
                         exchange_type='topic',
                         durable=False,
                         auto_delete=True,
                         arguments=None)

channel.exchange_declare(exchange=RESPONSE_TOPIC,
                         exchange_type='topic',
                         durable=False,
                         auto_delete=True,
                         arguments=None)

rcv_queue = channel.queue_declare("", auto_delete=True).method.queue

channel.queue_bind(rcv_queue, RESPONSE_TOPIC, RESPONSE_ROUTE)
print(f"Listening on {RESPONSE_TOPIC} - {RESPONSE_ROUTE}")


def consumer(ch, meth, pr, body):
    print("Received response: ")
    print(body.decode("utf-8"))
    print()

    if channel != None and channel.is_open:
        channel.close()
        print("Channel closed ... ")

    if connection != None and connection.is_open:
        connection.close()
        print("Connection closed ... ")


channel.basic_consume(rcv_queue, consumer)

new_game_data = JoinRoomMessage(ROOM_USER, ROOM_NAME, ROOM_PASSWORD)
str_new_game_data = jsonpickle.encode(new_game_data, unpicklable=False)

message_data = Message(MESSAGE_NAME, str_new_game_data)
str_message_data = jsonpickle.encode(message_data, unpicklable=False)

TARGET_STR_DATA = str_message_data

print(f"Publishing on: {PUBLISH_TOPIC} - {PUBLISH_ROUTE}")
print("Data: " + TARGET_STR_DATA)

channel.basic_publish(exchange=PUBLISH_TOPIC,
                      routing_key=PUBLISH_ROUTE,
                      body=TARGET_STR_DATA)

print("SENT ... ")


def sig_handler(sig, frame):

    print("\nCancellation requested ... ")

    if(channel is not None and channel.is_open):
        channel.close()
        print("Channel closed ... ")

    if(connection is not None and connection.is_open):
        connection.close()
        print("Connection closed ... ")

    sys.exit(0)


signal.signal(signal.SIGINT, sig_handler)

# blocking call
channel.start_consuming()
