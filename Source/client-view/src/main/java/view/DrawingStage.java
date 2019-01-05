package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.command.CommandQueue;
import view.command.DrawFieldCommand;
import view.component.Hexagon;

public class DrawingStage extends Stage implements View {

	private int STAGE_WIDTH = 800;
	private int STAGE_HEIGHT = 500;

	private Group root;
	private Scene main_scene;
	private Canvas board_canvas;

	private CommandQueue command_queue;
	private ExecutorService executor;
	private ViewCommandProcessor command_processor;

	private Map<String, List<ViewEventHandler>> handlers_map;

	public DrawingStage() {

		this.initStage();
		this.initCommandQueue();

		this.initEventHandlers();
	}

	// only stage specific things
	private void initStage() {
		// position on screen
		this.setX(100);
		this.setY(100);

		this.setWidth(this.STAGE_WIDTH);
		this.setHeight(this.STAGE_HEIGHT);

		// max/min width/height set for linux ( resizable(false) is not supported )
		this.setMaxWidth(this.STAGE_WIDTH);
		this.setMaxHeight(this.STAGE_HEIGHT);

		this.setMinWidth(this.STAGE_WIDTH);
		this.setMinHeight(this.STAGE_HEIGHT);

		this.setResizable(false); // doesn't work on linux
		this.initStyle(StageStyle.UNDECORATED); // removes title bar

		// create root node and add it to the main scene
		// Group is just container
		this.root = new Group();
		this.main_scene = new Scene(this.root);

		// create canvas and add it to the root node
		this.board_canvas = new Canvas();
		this.root.getChildren().add(this.board_canvas);

		// set canvas width/height to math stage size
		this.board_canvas.setWidth(this.STAGE_WIDTH);
		this.board_canvas.setHeight(this.STAGE_HEIGHT);

		// used for drawing things
		GraphicsContext gc = this.board_canvas.getGraphicsContext2D();

		// draw gray background
		gc.setFill(Color.GRAY);
		gc.fillRect(0, 0, this.STAGE_WIDTH, this.STAGE_HEIGHT);

		this.setScene(this.main_scene);
	}

	private void initCommandQueue() {

		this.command_queue = new CommandQueue();

		this.executor = Executors.newSingleThreadExecutor();

		this.command_processor = new ViewCommandProcessor(this.executor, this.board_canvas);

		this.command_queue = new CommandQueue();
		this.command_queue.setOnEnqueueEventHandler(this.command_processor);

	}

	private void initEventHandlers() {

		this.handlers_map = new HashMap<String, List<ViewEventHandler>>();

		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg) {

				Point2D field_position = Hexagon.calcStoragePosition(new Point2D(arg.getX(), arg.getY()),
						DrawFieldCommand.default_hex_size);

				String event_name = "";

				if (arg.getButton() == MouseButton.PRIMARY) {
					event_name = "left-mouse-click-event";
				} else if (arg.getButton() == MouseButton.SECONDARY) {
					event_name = "right-mouse-click-event";
				} else {
					event_name = "mouse-click-event";
				}

				List<ViewEventHandler> handlers_list = handlers_map.get(event_name);
				if (handlers_list != null) {

					for (ViewEventHandler handler : handlers_list) {
						handler.execute(new ViewEvent(arg, field_position));
					}
				}

			}

		});

		// key event handler name format
		// key-event-char-k - 'k' pressed button
		this.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {

				String key = event.getCharacter();
				System.out.println("Key pressed ... " + key);

				List<ViewEventHandler> handlers = handlers_map.get("key-event-char-" + key);
				if (handlers != null) {

					System.out.println("Found handlers for: key-event-char-" + key);

					for (ViewEventHandler handler : handlers) {
						System.out.println("Executing handler - " + ((NamedEventHandler) handler).getName());
						// handler.execute(event);
					}

				}

			}

		});

	}

	// CommandDrivernComponent methods
	public CommandQueue getCommandQueue() {
		return this.command_queue;
	}

	public void setCommandQueue(CommandQueue command_queue) {
		this.command_queue = command_queue;
	}

	// EventDrivenComponent methods
	public void addEventHandler(String event_name, ViewEventHandler event_handler) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		if (handlers != null) {

			handlers.add(event_handler);

		} else {

			handlers = new ArrayList<ViewEventHandler>();
			handlers.add(event_handler);

			this.handlers_map.put(event_name, handlers);
		}

	}

	public List<ViewEventHandler> getEventHandlers(String event_name) {
		return this.handlers_map.get(event_name);
	}

	public ViewEventHandler getSingleEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		for (ViewEventHandler handler : handlers) {
			if (((NamedEventHandler) handler).getName().equals(handler_name))
				return handler;
		}

		return null;

	}

	public ViewEventHandler removeEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		int index = 0;
		for (ViewEventHandler handler : handlers) {
			if (((NamedEventHandler) handler).getName().equals(handler_name)) {
				return handlers.remove(index);
			}
			index++;
		}

		return null;

	}

	public List<ViewEventHandler> removeAllEventHandlers(String event_name) {
		return this.handlers_map.remove(event_name);
	}

	// ShouldBeShutdown methods
	public void shutdown() {

		// called after application stop
		this.executor.shutdown();

	}

}
