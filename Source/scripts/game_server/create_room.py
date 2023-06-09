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

MESSAGE_NAME = "CreateRoomRequest"

ROOM_NAME = "some"
ROOM_PASSWORD = "room"
ROOM_USER = "some"

TARGET_TOPIC = "gg_rooms_req"
TARGET_ROUTE = "new_room" + "." + ROOM_NAME + "." + ROOM_USER

RESPONSE_TOPIC = "gg_rooms_res"
RESPONSE_ROUTE = "room_response." + ROOM_NAME + ".#"


class NewGameRequest():
    def __init__(self,
                 roomName: str,
                 password: str,
                 username: str):
        self.type = MESSAGE_NAME
        self.password = password
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

new_game_data = NewGameRequest(ROOM_NAME, ROOM_PASSWORD, ROOM_USER)
str_new_game_data = jsonpickle.encode(new_game_data, unpicklable=False)

message_data = Message(MESSAGE_NAME, str_new_game_data)
str_message_data = jsonpickle.encode(message_data, unpicklable=False)

TARGET_STR_DATA = str_message_data

print("Topic: " + TARGET_TOPIC)
print("Route: " + TARGET_ROUTE)
print("Data: " + TARGET_STR_DATA)

channel.basic_publish(exchange=TARGET_TOPIC,
                      routing_key=TARGET_ROUTE,
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
