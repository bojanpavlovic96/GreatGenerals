package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliInterface implements UserInterface, Runnable {

	private boolean running;

	private BufferedReader reader;

	private Thread game_thread;

	private Map<String, CliActionHandler> commands_map;

	public CliInterface() {

		this.running = false;

		this.reader = new BufferedReader(new InputStreamReader(System.in));

		this.commands_map = new HashMap<String, CliActionHandler>();

		this.populateCommandsMap();

	}

	public void run() {

		String user_input = "";

		try {
			while (this.running) {

				System.out.print("Waiting for user input: ");
				user_input = this.reader.readLine();

				if (this.running) {

					this.inputProcessor(user_input);

				}
			}

			System.out.println("Game loop definitely ended ... out of the while ...");

		} catch (Exception e) {
			System.out.println("Exception in game loop...");
			System.out.println(e);
		}

	}

	private void populateCommandsMap() {

		// add cliActionHandler-s for every command

		this.commands_map.put("prettyPrint", new CliActionHandler() {

			public void performAction(List<String> arguments) {
				int space = 1;
				for (String word : arguments) {

					for (int i = 0; i < space; i++)
						System.out.print("\t");

					System.out.println(word);
				}

				System.out.println(":)");

			}
		});

	}

	private void inputProcessor(String input) {
		System.out.println("Processing user input: " + input);

		// split input in to the list of words
		List<String> words = new ArrayList<String>(Arrays.asList(input.split(" ")));

		// get handler for first word (actual command)
		CliActionHandler input_handler = this.commands_map.get(words.get(0));

		String command = null;

		// if command exists
		if (input_handler != null) {

			System.out.println("Valid command...");

			command = words.remove(0);

			input_handler.performAction(words);

		}

		System.out.println("Command: " + command + " has been processed");
	}

	public void start() {
		this.running = true;

		this.game_thread = new Thread(this);

		this.game_thread.start();
	}

	public void stop() {
		if (this.running) {
			this.running = false;
			System.out.println("Press enter to stop game loop ...");
			System.out.println("Game loop should stop now...");
		} else {
			System.out.println("Game loop is not started yet ...");
		}
	}

	public void setClickHandler() {
		// TODO Auto-generated method stub

	}

}
