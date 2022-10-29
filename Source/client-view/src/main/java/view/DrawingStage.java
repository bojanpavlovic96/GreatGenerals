package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import root.Point2D;
import root.command.CommandDrivenComponent;
import root.command.CommandProcessor;
import root.command.CommandQueue;
import root.model.component.Field;
import root.view.View;
import root.view.ViewConfig;
import root.view.event.ViewEventArg;
import root.view.event.ViewEventHandler;
import root.view.field.ViewField;
import root.view.menu.Menu;
import view.command.FxCommandProcessor;
import view.component.ViewFieldManager;
import view.component.menu.LongOptionsMenu;
import view.component.menu.ShortOptionsMenu;

// attention 
// attention scroll pane (sometimes) throws some exception but it is harmless
// (somehow disappeared later in development... )

public class DrawingStage extends Stage implements View {

	private double STAGE_WIDTH;
	private double STAGE_HEIGHT;

	private double CANVAS_PADDING = 20;

	private ViewConfig config;

	private Group root;
	private ScrollPane scroll;
	private Scene mainScene;

	private Canvas boardCanvas;
	private Canvas secondLayerCanvas;

	private ShortOptionsMenu shortOptionsMenu;
	private LongOptionsMenu longOptionsMenu;

	private double canvasWidth;
	private double canvasHeight;

	private Color backgroundColor = Color.GRAY;

	// connection with controller
	private CommandQueue commandQueue;
	private CommandProcessor commandProcessor;

	private Map<String, List<ViewEventHandler>> handlersMap;

	// holds info about view fields and conversion methods
	private ViewFieldManager fieldManager;

	// constructors

	public DrawingStage(ViewFieldManager fieldManager, ViewConfig config) {
		super();

		this.config = config;

		this.fieldManager = fieldManager;

		this.initStage();

		this.initEventHandlers();
	}

	// only stage specific things

	private void initStage() {

		this.setFullScreen(true);
		// disable exit from full screen
		this.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		// this.initStyle(StageStyle.UNDECORATED);

		// create root node and add it to the main scene
		// Group is just container
		this.root = new Group();
		this.scroll = new ScrollPane(root);
		this.mainScene = new Scene(scroll);

		// hide scroll bars
		this.scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scroll.setVbarPolicy(ScrollBarPolicy.NEVER);

		// stage size match screen size because of full screen mode
		this.STAGE_WIDTH = Screen.getPrimary().getBounds().getWidth();
		this.STAGE_HEIGHT = Screen.getPrimary().getBounds().getHeight();

		// match canvas size with stage (windows) size
		this.canvasWidth = this.STAGE_WIDTH;
		this.canvasHeight = this.STAGE_HEIGHT;

		// create canvas and add it to the root node
		this.boardCanvas = new Canvas();

		// set canvas width/height to match stage size
		this.boardCanvas.setWidth(this.canvasWidth);
		this.boardCanvas.setHeight(this.canvasHeight);

		this.root.getChildren().add(this.boardCanvas);

		// second canvas on top of the first one used for animations and field options
		this.secondLayerCanvas = new Canvas();

		// width and height same as the board canvas
		this.secondLayerCanvas.setWidth(this.canvasWidth);
		this.secondLayerCanvas.setHeight(this.canvasHeight);

		this.root.getChildren().add(this.secondLayerCanvas);

		System.out.println("Initial canvas width: " + this.canvasWidth);
		System.out.println("Initial canvas height: " + this.canvasHeight);

		this.shortOptionsMenu = new ShortOptionsMenu(
				config.fieldMenuWidth,
				config.fieldMenuHeight);

		this.longOptionsMenu = new LongOptionsMenu(
				config.fieldMenuWidth,
				config.fieldMenuHeight);

		this.shortOptionsMenu.setLayoutX(100);
		this.shortOptionsMenu.setLayoutY(100);
		this.shortOptionsMenu.setVisible(true);

		Platform.setImplicitExit(true);

		this.root.getChildren().add(this.shortOptionsMenu);

		this.setScene(this.mainScene);
	}

	private void initEventHandlers() {

		this.handlersMap = new HashMap<String, List<ViewEventHandler>>();

		var eventHandler = new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg) {
				Point2D fieldPosition = fieldManager
						.calcStoragePosition(new Point2D(arg.getX(), arg.getY()));

				String eventName = "";

				if (arg.getButton() == MouseButton.PRIMARY) {
					eventName = "left-mouse-click-event";
				} else if (arg.getButton() == MouseButton.SECONDARY) {
					eventName = "right-mouse-click-event";
				} else {
					eventName = "mouse-click-event";
				}

				List<ViewEventHandler> handlersList = handlersMap.get(eventName);
				if (handlersList != null) {

					for (ViewEventHandler handler : handlersList) {
						handler.execute(new ViewEventArg(fieldPosition));
					}

				}
			}
		};
		secondLayerCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

		// key event handler name format
		// key-event-char-k - 'k' pressed button
		addEventHandler(KeyEvent.KEY_TYPED,
				(event) -> {

					String key = event.getCharacter();

					List<ViewEventHandler> handlers = handlersMap.get("key-event-char-" + key);
					if (handlers != null) {

						for (ViewEventHandler handler : handlers) {
							// argument of the execute method is never used for key pressed event...
							// it is ok to be null
							// TODO make null disappear
							handler.execute(null);
						}

					}

				});

	}

	// public methods

	// CommandDrivenComponent methods

	@Override
	public CommandQueue getCommandQueue() {
		return this.commandQueue;
	}

	@Override
	public void setCommandQueue(CommandQueue commandQueue) {
		this.commandQueue = commandQueue;

		this.commandProcessor = new FxCommandProcessor((CommandDrivenComponent) this);
		this.commandQueue.setCommandProcessor(this.commandProcessor);

	}

	// TODO replace name with an enum
	@Override
	public void addEventHandler(String event_name, ViewEventHandler event_handler) {

		List<ViewEventHandler> handlers = this.handlersMap.get(event_name);

		if (handlers != null) {

			handlers.add(event_handler);

		} else {

			handlers = new ArrayList<ViewEventHandler>();
			handlers.add(event_handler);

			this.handlersMap.put(event_name, handlers);
		}

	}

	// event producer interface

	@Override
	public List<ViewEventHandler> getEventHandlers(String event_name) {
		return this.handlersMap.get(event_name);
	}

	@Override
	public ViewEventHandler getSingleEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlersMap.get(event_name);

		for (ViewEventHandler handler : handlers) {
			if (((NamedEventHandler) handler).getName().equals(handler_name))
				return handler;
		}

		return null;

	}

	@Override
	public ViewEventHandler removeEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlersMap.get(event_name);

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
		return this.handlersMap.remove(event_name);
	}

	@Override
	public void shutdown() {
		// got there from CommandDrivenComponent
		// in Controller command processing is done using threadPool
		// in this case command processing is done using Platform.runLater
	}

	// view interface
	@Override
	public void showView() {
		Platform.runLater(() -> {
			show();
		});
	}

	@Override
	public String getViewTheme() {
		return ResourceManager.getAssetsType();
	}

	@Override
	public boolean zoomIn() {
		return this.fieldManager.zoomIn();
	}

	@Override
	public boolean zoomOut() {
		return this.fieldManager.zoomOut();
	}

	@Override
	public void setCanvasVisibility(boolean visibility) {
		this.boardCanvas.setVisible(visibility);
		this.secondLayerCanvas.setVisible(visibility);
	}

	@Override
	public ViewField convertToViewField(Field model) {
		return this.fieldManager.getViewField(model);
	}

	@Override
	public double getFieldHeight() {
		return this.fieldManager.getHeight();
	}

	@Override
	public double getFieldWidth() {
		return this.fieldManager.getWidth();
	}

	@Override
	public double getFieldBorderWidth() {
		return this.fieldManager.getBorderWidth();
	}

	@Override
	public GraphicsContext getMainGraphicContext() {
		return this.boardCanvas.getGraphicsContext2D();
	}

	@Override
	public GraphicsContext getTopLayerGraphicContext() {
		return this.secondLayerCanvas.getGraphicsContext2D();
	}

	@Override
	public void setMenuVisibility(boolean visibility) {
		// with next line, vbox throws some outOfBoundsException: -1 ... don't touch it
		// not anymore, but I will leave above comment ... just in case
		this.shortOptionsMenu.setVisible(visibility);
	}

	@Override
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public CommandProcessor getCommandProcessor() {
		return this.commandProcessor;
	}

	@Override
	public Menu getShortOptionMenu() {
		return this.shortOptionsMenu;
	}

	@Override
	public Menu getLongOptionsMenu() {
		return this.longOptionsMenu;
	}

	@Override
	public void setMenuPosition(Point2D position) {
		this.shortOptionsMenu.setPosition(position);
	}

	@Override
	public void adjustCanvasSize(Point2D point) {

		Point2D real_position = this.fieldManager
				.calcRealPosition(new Point2D(point.getX() + 1, point.getY() + 1));

		if (real_position.getX() > this.STAGE_WIDTH)
			this.canvasWidth = real_position.getX();
		else
			this.canvasWidth = this.STAGE_WIDTH;

		if (real_position.getY() > this.STAGE_HEIGHT)
			this.canvasHeight = real_position.getY();
		else
			this.canvasHeight = this.STAGE_HEIGHT;

		this.boardCanvas.setWidth(this.canvasWidth);
		this.secondLayerCanvas.setWidth(this.canvasWidth);

		this.boardCanvas.setHeight(this.canvasHeight);
		this.secondLayerCanvas.setHeight(this.canvasHeight);

	}

	@Override
	public void hideView() {
		Platform.runLater(() -> {
			hide();
		});
	}

}
