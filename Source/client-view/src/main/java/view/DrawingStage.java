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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import root.command.BasicCommandProcessor;
import root.command.CommandProcessor;
import root.command.CommandQueue;
import root.model.component.Field;
import root.view.View;
import root.view.event.ViewEventArg;
import root.view.event.ViewEventHandler;
import root.view.field.ViewField;
import root.view.menu.Menu;
import view.component.ViewFieldManager;
import view.component.menu.Option;
import view.component.menu.OptionMenu;

// attention 
// attention scroll pane (sometimes) throws some exception but it is harmless
// attention 

public class DrawingStage extends Stage implements View {

	private double STAGE_WIDTH;
	private double STAGE_HEIGHT;

	private double CANVAS_PADDING = 20;

	private Group root;
	private ScrollPane scroll;
	private Scene main_scene;

	private Canvas board_canvas;
	private Canvas second_layer_canvas;

	private ScrollPane menu_scroll;
	private OptionMenu field_menu;

	private double canvas_width;
	private double canvas_height;

	private Color background_color = Color.GRAY;

	// connection with controller
	private CommandQueue command_queue;
	private ExecutorService executor;
	private CommandProcessor command_processor;

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
		this.canvas_width = dimension.getWidth();
		this.canvas_height = dimension.getHeight();

		// create canvas and add it to the root node
		this.board_canvas = new Canvas();

		// set canvas width/height to match stage size
		this.board_canvas.setWidth(this.canvas_width);
		this.board_canvas.setHeight(this.canvas_height);

		this.root.getChildren().add(this.board_canvas);

		// second canvas on top of the first one used for animations and field options
		this.second_layer_canvas = new Canvas();

		// width and height same as the board canvas
		this.second_layer_canvas.setWidth(this.canvas_width);
		this.second_layer_canvas.setHeight(this.canvas_height);

		this.root.getChildren().add(this.second_layer_canvas);

		System.out.println("Initial canvas width: " + this.canvas_width);
		System.out.println("Initial canvas heigth: " + this.canvas_height);

		// attention adjust size
		this.field_menu = new OptionMenu(100, 100);
		this.field_menu.addOption(new Option("option"));
		// scroll wrapper
		this.menu_scroll = new ScrollPane(this.field_menu);
		this.menu_scroll.setPrefSize(100, 200);
		this.menu_scroll.setStyle("-fx-background-color: gray;");

		this.field_menu.setLayoutX(100);
		this.field_menu.setLayoutY(100);
		this.field_menu.setVisible(true);

		this.root.getChildren().add(this.field_menu);
		this.root.getChildren().add(this.menu_scroll);

		this.setScene(this.main_scene);

	}

	private void initCommandQueue() {

		this.command_queue = new CommandQueue();

		this.executor = Executors.newSingleThreadExecutor();

		this.command_processor = new BasicCommandProcessor(this.executor, this);

		this.command_queue.setCommandProcessor(this.command_processor);

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
						handler.execute(new ViewEventArg(arg, field_position));
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

	// attention just testing
	private void fillBackground() {

		GraphicsContext gc = this.board_canvas.getGraphicsContext2D();

		gc.save();

		gc.setFill(Color.GRAY);

		gc.fillRect(0, 0, this.canvas_width, this.canvas_height);

		gc.restore();

	}

	// public methods

	// CommandDrivernComponent methods

	@Override
	public CommandQueue getCommandQueue() {
		return this.command_queue;
	}

	@Override
	public void setCommandQueue(CommandQueue command_queue) {
		this.command_queue = command_queue;
	}

	@Override
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

	@Override
	public List<ViewEventHandler> getEventHandlers(String event_name) {
		return this.handlers_map.get(event_name);
	}

	@Override
	public ViewEventHandler getSingleEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		for (ViewEventHandler handler : handlers) {
			if (((NamedEventHandler) handler).getName().equals(handler_name))
				return handler;
		}

		return null;

	}

	@Override
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

	@Override
	public List<ViewEventHandler> removeAllEventHandlers(String event_name) {
		return this.handlers_map.remove(event_name);
	}

	@Override
	public void shutdown() {

		// called after application stop
		this.executor.shutdown();

	}

	// view interface

	// show() is already implemented in stage

	@Override
	public String getViewTheme() {
		return ResourceManager.getAssetsType();
	}

	@Override
	public boolean zoomIn() {
		return this.field_manager.zoomIn();
	}

	@Override
	public boolean zoomOut() {
		return this.field_manager.zoomOut();
	}

	@Override
	public void setCanvasVisibility(boolean visibilibity) {
		this.board_canvas.setVisible(visibilibity);
		this.second_layer_canvas.setVisible(visibilibity);
	}

	@Override
	public ViewField convertToViewField(Field model) {

		return this.field_manager.getViewField(model);

	}

	@Override
	public double getFieldHeight() {
		return this.field_manager.getHeight();
	}

	@Override
	public double getFieldWidth() {
		return this.field_manager.getWidth();
	}

	@Override
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

			// debug
			System.out.println("Adjusted ... ");
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

			// debug
			System.out.println("Adjusted ... ");

		}

	}

	public void singleAdjust(Field width_model, Field height_model) {

		ViewField view_width_model = this.field_manager.getViewField(width_model);
		ViewField view_height_model = this.field_manager.getViewField(height_model);

		Point2D right_center = view_width_model.getFieldCenter();
		Point2D down_center = view_height_model.getFieldCenter();

		if (right_center.getX() > down_center.getX()) {
			this.canvas_width = right_center.getX();
		} else {
			this.canvas_width = down_center.getX();
		}

		if (right_center.getY() > down_center.getY()) {
			this.canvas_height = right_center.getY();
		} else {
			this.canvas_height = down_center.getY();
		}

		this.canvas_width += this.field_manager.getWidth() / 2 + this.CANVAS_PADDING;
		this.canvas_height += this.field_manager.getHeight() / 2 + this.CANVAS_PADDING;

		this.board_canvas.setWidth(this.canvas_width);
		this.board_canvas.setHeight(this.canvas_height);

		this.second_layer_canvas.setWidth(this.canvas_width);
		this.second_layer_canvas.setHeight(this.canvas_height);

		this.fillBackground();

		// debug
		System.out.println("single adjusted");
		System.out.println("W: " + this.canvas_width);
		System.out.println("H: " + this.canvas_height);

	}

	@Override
	public void adjustCanvasSize() {
	}

	@Override
	public GraphicsContext getMainGraphicContext() {
		return this.board_canvas.getGraphicsContext2D();
	}

	@Override
	public GraphicsContext getTopLayerGraphicContext() {
		return this.second_layer_canvas.getGraphicsContext2D();
	}

	@Override
	public void setMenuVisibility(boolean visibility) {
		this.field_menu.setVisible(visibility);
		this.menu_scroll.setVisible(visibility);
		// debug
		System.out.println("Menu visibility set to : " + visibility);
	}

	@Override
	public Color getBackgroundColor() {
		return this.background_color;
	}

	@Override
	public CommandProcessor getCommandProcessor() {
		return this.command_processor;
	}

	@Override
	public Menu getOptionMenu() {
		return this.field_menu;
	}

	@Override
	public void setMenuPosition(Point2D position) {
		this.field_menu.setPosition(position);

		this.menu_scroll.setLayoutX(position.getX());
		this.menu_scroll.setLayoutY(position.getY());
	}

}
