package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import board.BoardManager;
import chat.BasicChatController;
import chat.ChatController;
import rabbit.api.Messenger;
import rabbit.api.Pigeon;
import ui.CliInterface;
import ui.UserInterface;

public class Client {

	private Messenger messenger;

	private ChatController chat_controller;

	private BoardManager board_manager;

	private GameData game_data;

	private UserInterface user_interface;

	public Client(Messenger messenger, ChatController chat_controller, BoardManager board_manager,
			GameData game_data, UserInterface user_interface) {

		this.messenger = messenger;
		this.chat_controller = chat_controller;
		this.board_manager = board_manager;
		this.game_data = game_data;
		this.user_interface = user_interface;

		// set handlers to all available events
		this.connectWithUserInterface();

		this.user_interface.start();

		System.out.println("Client created...");

	}

	private void connectWithUserInterface() {

	}

	public static void main(String[] args) {

		Messenger messenger = new Pigeon("amqp://guest:guest@cera.ddns.net");
		ChatController chat_controller = new BasicChatController();
		BoardManager board_manager = null;
		GameData game_data = new GameData();
		UserInterface user_interface = new CliInterface();

		Client client = new Client(messenger, chat_controller, board_manager, game_data, user_interface);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String user_input = "";

		try {

			while (!user_input.equals("cancel")) {

				System.out.print("Waiting for user input: ");

				user_input = reader.readLine();

				System.out.println("Processing: " + user_input);

			}

		} catch (Exception e) {
			System.out.println("Exception in game loop...");
			System.out.println(e);
		}

	}

}
