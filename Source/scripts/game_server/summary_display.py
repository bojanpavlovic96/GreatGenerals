import signal
from time import sleep
import jsonpickle
import sys
import requests
import os

SERVER_ADDRESS = "localhost"
SERVER_PORT = 9001
POOL_SUMMARY_PATH = "/summary/pool"
GAME_SUMMARY_PATH = "/summary/game"

SLEEP_PERIOD = 2000
JSON_INDENT = 4

running = True


def clear_console():
    command = 'clear'
    if os.name in ('nt', 'dos'):  # If Machine is running on Windows, use cls
        command = 'cls'
    os.system(command)


def sig_handler(sig, frame):

    print("\nCancellation requested ... ")

    running = False

    sys.exit(0)


signal.signal(signal.SIGINT, sig_handler)

pool_url = f"http://{SERVER_ADDRESS}:{str(SERVER_PORT)}{POOL_SUMMARY_PATH}"
game_url = f"http://{SERVER_ADDRESS}:{str(SERVER_PORT)}{GAME_SUMMARY_PATH}"

pull_count = 0

while(running):
    clear_console()
    print(f" Pull count: {str(pull_count)}\n")
    print(" " + pool_url)
    print(" ================ POOL SUMMARY ================")
    print("")
    pool_summary = requests.get(pool_url).content.decode("UTF-8")
    json_pools = jsonpickle.loads(pool_summary)
    print(jsonpickle.dumps(json_pools, indent=JSON_INDENT))

    print("")
    print(" " + game_url)
    print(" ================ GAMES SUMMARY ================")
    print("")

    games_summary = requests.get(game_url).content.decode("UTF-8")
    json_games = jsonpickle.loads(games_summary)
    print(jsonpickle.dumps(json_games, indent=JSON_INDENT))

    sleep(2)
    pull_count += 1
