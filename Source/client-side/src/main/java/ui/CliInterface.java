package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CliInterface implements UserInterface, Runnable {

	private boolean running;

	private BufferedReader reader;

	private Thread game_thread;

	private Map<String, CliActionHandler> commands_map;

	private Map<String, List<UIEventHandler>> handlers_map;

	public CliInterface() {

		this.running = false;

		this.reader = new BufferedReader(new InputStreamReader(System.in));

		this.commands_map = new HashMap<String, CliActionHandler>();

		this.handlers_map = new HashMap<String, List<UIEventHandler>>();

		this.populateCommandsMap();

	}

	// Runnable mehod
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

					space++;
				}

				System.out.println(":)");

			}
		});

		this.commands_map.put("exit", new CliActionHandler() {
			public void performAction(List<String> arguments) {
				running = false;
			}
		});

	}

	private void inputProcessor(String input) {
		System.out.println("Processing user input: " + input);

		// split input in to the list of words
		List<String> words = new ArrayList<String>(Arrays.asList(input.split(" ")));

		// first word is command
		String command = words.remove(0);

		// get handler for first word (actual command)
		CliActionHandler input_handler = this.commands_map.get(command);

		// if command exists
		if (input_handler != null) {

			System.out.println("Valid command...");

			input_handler.performAction(words);

		} else {
			System.out.println("Unknown command ...");
		}

		System.out.println("Command: " + command + " has been processed");
	}

	// UserInterface methods
	public void startGameLoop() {
		this.running = true;

		this.game_thread = new Thread(this);

		this.game_thread.start();
	}

	public void stopGameLoop() {
		if (this.running) {
			this.running = false;
			System.out.println("Press enter to stop game loop ...");
			System.out.println("Game loop should stop now ...");
		} else {
			System.out.println("Game loop is not started yet ...");
		}
	}

	public void addEventHandler(String event_name, UIEventHandler event_handler) {

		List<UIEventHandler> handlers = this.handlers_map.get(event_name);

		if (handlers != null) {

			handlers.add(event_handler);

		} else {

			handlers = new ArrayList<UIEventHandler>();
			handlers.add(event_handler);

			this.handlers_map.put(event_name, handlers);
		}

	}

	public List<UIEventHandler> getEventHandlers(String event_name) {
		return this.handlers_map.get(event_name);
	}

	public UIEventHandler getSingleEventHandler(String event_name, String handler_name) {

		List<UIEventHandler> handlers = this.handlers_map.get(event_name);

		for (UIEventHandler handler : handlers) {
			if (((NamedUIEventHandler) handler).getName().equals(handler_name))
				return handler;
		}

		return null;

	}

	public boolean removeEventHandler(String event_name, String handler_name) {
		List<UIEventHandler> handlers = this.handlers_map.get(event_name);

		int index = 0;
		for (UIEventHandler handler : handlers) {
			if (((NamedUIEventHandler) handler).getName().equals(handler_name)) {
				handlers.remove(index);
				return true;
			}
			index++;
		}

		return false;
	}

}
