package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import board.BoardManager;
import chat.BasicChatController;
import chat.ChatController;
import rabbit.api.Messenger;
import rabbit.api.Pigeon;

public class Client {

	private Messenger messenger;

	private ChatController chat_controller;

	private BoardManager board_manager;

	private GameData game_data;

	public Client(Messenger messenger, ChatController chat_controller, BoardManager board_manager,
			GameData game_data) {

		this.messenger = messenger;
		this.chat_controller = chat_controller;
		this.board_manager = board_manager;
		this.game_data = game_data;

		System.out.println("CliClient created...");

	}

	public static void main(String[] args) {

		Messenger messenger = new Pigeon("amqp://guest:guest@cera.ddns.net");
		ChatController chat_controller = new BasicChatController();
		BoardManager board_manager = null;
		GameData game_data = new GameData();

		Client client = new Client(messenger, chat_controller, board_manager, game_data);

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
