package view;

import java.awt.Dimension;
import java.awt.Toolkit;
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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.component.field.Field;
import view.command.CommandQueue;
import view.command.ViewCommandProcessor;
import view.component.ViewField;
import view.component.ViewFieldManager;
import view.component.menu.FieldMenu;
import view.component.menu.OptionMenu;

// attention 
// attention scroll pane throws some exception but it is harmless
// attention 
// drawing stage (as view) only provides base for "drawing" game
public class DrawingStage extends Stage implements View {

	private double STAGE_WIDTH;
	private double STAGE_HEIGHT;

	private double CANVAS_PADDING = 20;

	// attention externalize it somehow
	private Color background_color = Color.WHITE;

	private Group root;
	private ScrollPane scroll;
	private Scene main_scene;

	private Canvas board_canvas;
	private Canvas second_layer_canvas;
	private OptionMenu field_menu;

	private double canvas_width;
	private double canvas_height;

	// TODO initialize
	private double last_h_field;
	private double last_v_field;

	// connection with controller
	private CommandQueue command_queue;
	private ExecutorService executor;
	private ViewCommandProcessor command_processor;

	private Map<String, List<ViewEventHandler>> handlers_map;

	// holds info about view fields and conversion methods
	private ViewFieldManager field_manager;

	// constructors

	public DrawingStage(ViewFieldManager field_converter) {
		super();

		this.field_manager = field_converter;

		this.initStage();

		this.initCommandQueue();

		this.initEventHandlers();

	}

	// only stage specific things

	private void initStage() {
		// position on screen
		// this.setX(100);
		// this.setY(100);

		// set full screen mode
		this.setFullScreen(true);
		// disable exit from full screen
		this.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		// this.initStyle(StageStyle.UNDECORATED);

		// create root node and add it to the main scene
		// Group is just container
		this.root = new Group();
		this.scroll = new ScrollPane(root);
		this.main_scene = new Scene(scroll);

		// hide scroll bars
		this.scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scroll.setVbarPolicy(ScrollBarPolicy.NEVER);

		// get screen size
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		// stage size match screen size because of full screen mode
		this.STAGE_WIDTH = dimension.getWidth();
		this.STAGE_HEIGHT = dimension.getHeight();

		// initially set canvas size to something smaller than stage size
		// it is going to be resized when the board is loaded (loadBoardCommand)
		// this.canvas_width = this.STAGE_WIDTH / 2;
		// this.canvas_height = this.STAGE_HEIGHT / 2;
		this.canvas_width = 1;
		this.canvas_height = 1;

		// initial value same as canvas size
		this.last_h_field = 1;
		this.last_h_field = 1;

		// create canvas and add it to the root node
		this.board_canvas = new Canvas();

		// set canvas width/height to match stage size
		this.board_canvas.setWidth(this.canvas_width);
		this.board_canvas.setHeight(this.canvas_height);

		this.root.getChildren().add(this.board_canvas);

		// second canvas on top of the first one used for animations and field options
		this.second_layer_canvas = new Canvas();

		this.second_layer_canvas.setWidth(this.canvas_width);
		this.second_layer_canvas.setHeight(this.canvas_height);

		this.root.getChildren().add(this.second_layer_canvas);

		System.out.println("Initial canvas width: " + this.canvas_width);
		System.out.println("Initial canvas heigth: " + this.canvas_height);

		this.field_menu = new FieldMenu();
		this.field_menu.setVisible(false);

		// attention buttons used just for testing
		Button b1 = new Button("button 1");
		Button b2 = new Button("button 2");
		Button b3 = new Button("button 3");
		this.root.getChildren().add(this.field_menu);
		this.field_menu.getChildren().add(b1);
		this.field_menu.getChildren().add(b2);
		this.field_menu.getChildren().add(b3);

		this.setScene(this.main_scene);

	}

	private void initCommandQueue() {

		this.command_queue = new CommandQueue();

		this.executor = Executors.newSingleThreadExecutor();

		this.command_processor = new ViewCommandProcessor(this.executor, this);

		this.command_queue.setOnEnqueueEventHandler(this.command_processor);

	}

	private void initEventHandlers() {

		this.handlers_map = new HashMap<String, List<ViewEventHandler>>();

		this.second_layer_canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg) {

				Point2D field_position = field_manager.calcStoragePosition(new Point2D(	arg.getX(),
																						arg.getY()));

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

				List<ViewEventHandler> handlers = handlers_map.get("key-event-char-" + key);
				if (handlers != null) {

					for (ViewEventHandler handler : handlers) {
						// argument of the execute method is never used for key pressed event...
						// it is ok to be null
						// TODO make null disappear
						handler.execute(null);
					}

				}

			}

		});

	}

	// public methods

	// CommandDrivernComponent methods
	// command driven component

	public CommandQueue getCommandQueue() {
		return this.command_queue;
	}

	public void setCommandQueue(CommandQueue command_queue) {
		this.command_queue = command_queue;
	}

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

	// event driven component interface

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

	// should be shoutDown interface

	public void shutdown() {

		// called after application stop
		this.executor.shutdown();

	}

	// layered view methods

	// layered view interface

	public Canvas getMainCanvas() {
		return this.board_canvas;
	}

	public Canvas getTopLayerCanvas() {
		return this.second_layer_canvas;
	}

	public OptionMenu getFieldMenu() {
		return this.field_menu;
	}

	public Group getRootContainer() {
		return this.root;
	}

	public Color getBackgroundColor() {
		return background_color;
	}

	public double getStageWidth() {
		return this.getWidth();
	}

	public double getStageHeight() {
		return this.getHeight();
	}

	// view interface

	// show() is already implemented in stage

	public String getViewType() {
		return ResourceManager.getAssetsType();
	}

	public boolean zoomIn() {
		return this.field_manager.zoomIn();
	}

	public boolean zoomOut() {
		return this.field_manager.zoomOut();
	}

	public void setCanvasVisibility(boolean visibilibity) {
		this.board_canvas.setVisible(visibilibity);
	}

	public ViewField convertToViewField(Field model) {

		return this.field_manager.getViewField(model);

	}

	public double getFieldHeight() {
		return this.field_manager.getHeight();
	}

	public double getFieldWidth() {
		return this.field_manager.getWidth();
	}

	public double getFieldBorderWidth() {
		return this.field_manager.getBorderWidth();
	}

	public void adjustCanvasSize(ViewField field) {
		
		Point2D position = field.getFieldCenter();

		if (position.getX() > this.canvas_width - this.CANVAS_PADDING) {

			this.canvas_width = position.getX() + this.field_manager.getWidth()
								+ 2 * this.field_manager.getBorderWidth()
								+ this.CANVAS_PADDING;

			this.board_canvas.setWidth(this.canvas_width);
			this.second_layer_canvas.setWidth(this.canvas_width);

			// center canvas horizontally if it is smaller than the stage
			if (this.canvas_width < this.STAGE_WIDTH) {
				double h_difference = this.STAGE_WIDTH - this.canvas_width;

				this.board_canvas.setLayoutX(h_difference / 2);
				this.second_layer_canvas.setLayoutX(h_difference / 2);

			}

		}

		if (position.getY() > this.canvas_height - this.CANVAS_PADDING) {

			this.canvas_height = position.getY() + this.field_manager.getHeight()
									+ 2 * this.field_manager.getBorderWidth()
									+ this.CANVAS_PADDING;

			this.board_canvas.setHeight(this.canvas_height);
			this.second_layer_canvas.setHeight(this.canvas_height);

			// center canvas vertically if it is smaller than the stage
			if (this.canvas_height < this.STAGE_HEIGHT) {

				double v_difference = this.STAGE_HEIGHT - this.canvas_height;

				this.board_canvas.setLayoutY(v_difference / 2);
				this.second_layer_canvas.setLayoutY(v_difference / 2);

			}

		}

	}

	public double getCanvasWidth() {
		return this.canvas_width;
	}

	public double getCanvasHeight() {
		return this.canvas_height;
	}

}
