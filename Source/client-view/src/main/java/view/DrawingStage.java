package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import root.ActiveComponent;
import root.Point2D;
import root.command.CommandQueue;
import view.command.FxQueue;
import root.model.component.Field;
import root.view.View;
import root.view.ViewConfig;
import root.view.event.ViewEventArg;
import root.view.event.ViewEventHandler;
import root.view.field.ViewField;
import root.view.menu.FieldDescription;
import root.view.menu.Menu;
import view.component.ViewFieldManager;
import view.component.menu.DescriptionMenu;
import view.component.menu.ShortOptionsMenu;

public class DrawingStage extends Stage implements View {

	private double STAGE_WIDTH;
	private double STAGE_HEIGHT;

	private ViewConfig config;

	private Group root;
	private ScrollPane scroll;
	private Scene mainScene;

	private Canvas boardCanvas;
	private Canvas secondLayerCanvas;

	private ShortOptionsMenu mainOptionsMenu;
	private ShortOptionsMenu submenu;
	private DescriptionMenu descriptionMenu;

	private HBox textUiRoot;
	private VBox pointsCoinsRoot;
	private Label winnerUi;
	private Label coinsText;
	private Label pointsUi;

	private double canvasWidth;
	private double canvasHeight;

	private Color backgroundColor = Color.GRAY;

	// connection with controller
	private CommandQueue commandQueue;
	// private CommandProcessor commandProcessor;

	private Map<String, List<ViewEventHandler>> handlersMap;

	// holds info about view fields and conversion methods
	private ViewFieldManager fieldManager;

	// constructors

	public DrawingStage(ViewFieldManager fieldManager, ViewConfig config) {
		super();

		this.config = config;

		this.fieldManager = fieldManager;

		this.commandQueue = new FxQueue(this);

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

		this.mainOptionsMenu = new ShortOptionsMenu(
				config.fieldMenuWidth,
				config.fieldMenuHeight);

		this.submenu = new ShortOptionsMenu(
				config.fieldMenuWidth,
				config.fieldMenuHeight);

		this.descriptionMenu = new DescriptionMenu(config.descriptionWidth,
				config.descriptionHeight);

		this.mainOptionsMenu.setVisible(true);
		this.submenu.setVisible(true);

		this.descriptionMenu.setVisible(true);

		this.textUiRoot = new HBox();
		this.textUiRoot.setMouseTransparent(true);
		this.textUiRoot.setMaxWidth(this.canvasWidth);
		this.textUiRoot.setMaxHeight(this.canvasHeight);
		this.textUiRoot.setMinWidth(this.canvasWidth);
		this.textUiRoot.setMinHeight(this.canvasHeight);
		this.textUiRoot.setAlignment(Pos.TOP_RIGHT);

		this.winnerUi = new Label();
		this.winnerUi.setFont(new Font("Chilanka", 40));
		this.winnerUi.setStyle("-fx-background-color: #ff0000"); // TODO move to config
		this.winnerUi.setPadding(new Insets(20, 50, 20, 20));
		this.winnerUi.setVisible(false);

		this.textUiRoot.getChildren().add(this.winnerUi);

		this.pointsCoinsRoot = new VBox();

		this.textUiRoot.getChildren().add(pointsCoinsRoot);

		var coinsImage = new ImageView(ResourceManager.getInstance().getCoins());
		coinsImage.setFitWidth(100);
		coinsImage.setFitHeight(100);

		this.coinsText = new Label();
		this.coinsText.setFont(new Font("Chilanka", 30));
		this.coinsText.setPadding(new Insets(20, 20, 20, 40));
		this.coinsText.setText(formCoinsString(0));

		var coinsBox = new HBox(coinsImage, coinsText);
		coinsBox.setAlignment(Pos.CENTER_LEFT);

		this.pointsCoinsRoot.getChildren().add(coinsBox);

		this.pointsUi = new Label();
		this.pointsUi.setFont(new Font("Chilanka", 30));
		this.pointsUi.setPadding(new Insets(20, 20, 20, 40));

		this.pointsUi.setText(formPointsString(0));
		this.pointsCoinsRoot.getChildren().add(this.pointsUi);

		this.root.getChildren().add(this.mainOptionsMenu);
		this.root.getChildren().add(this.submenu);
		this.root.getChildren().add(this.descriptionMenu);
		this.root.getChildren().add(this.textUiRoot);

		Platform.setImplicitExit(true);

		this.setScene(this.mainScene);
	}

	private void initEventHandlers() {

		this.handlersMap = new HashMap<String, List<ViewEventHandler>>();

		// var eventHandler = new EventHandler<MouseEvent>() {

		// 	public void handle(MouseEvent arg) {
		// 		Point2D fieldPosition = fieldManager
		// 				.calcStoragePosition(new Point2D(arg.getX(), arg.getY()));

		// 		String eventName = "";

		// 		if (arg.getButton() == MouseButton.PRIMARY) {
		// 			eventName = "left-mouse-click-event";
		// 		} else if (arg.getButton() == MouseButton.SECONDARY) {
		// 			eventName = "right-mouse-click-event";
		// 		} else {
		// 			eventName = "mouse-click-event";
		// 		}

		// 		List<ViewEventHandler> handlersList = handlersMap.get(eventName);
		// 		if (handlersList != null) {

		// 			for (ViewEventHandler handler : handlersList) {
		// 				handler.execute(new ViewEventArg(fieldPosition));
		// 			}

		// 		}
		// 	}
		// };

		secondLayerCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
				(MouseEvent arg) -> {
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
				});

		// key event handler name format
		// key-event-char-k - 'k' pressed button
		addEventHandler(KeyEvent.KEY_TYPED,
				(event) -> {

					String key = event.getCharacter();

					System.out.println("Key typed: " + key);

					List<ViewEventHandler> handlers = handlersMap.get("key-event-char-" + key);
					if (handlers != null) {

						for (ViewEventHandler handler : handlers) {
							handler.execute(new ViewEventArg(key));
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

	// @Override
	// public void setCommandQueue(CmdQueue commandQueue) {
	// 	this.commandQueue = commandQueue;

	// 	this.commandProcessor = new FxCommandProcessor((CommandDrivenComponent) this);
	// 	this.commandQueue.setCommandProcessor(this.commandProcessor);

	// }

	// TODO replace name with an enum
	@Override
	public void addEventHandler(String eventName, ViewEventHandler event_handler) {

		List<ViewEventHandler> handlers = this.handlersMap.get(eventName);

		if (handlers != null) {
			handlers.add(event_handler);
		} else {

			handlers = new ArrayList<ViewEventHandler>();
			handlers.add(event_handler);

			this.handlersMap.put(eventName, handlers);
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

	// Deprecated since activeComponent is removed from CommandDriveComponent
	// @Override
	// public void shutdown() {
	// 	// got there from CommandDrivenComponent
	// 	// in Controller command processing is done using threadPool
	// 	// in this case command processing is done using Platform.runLater
	// }

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
		// With next line, vbox throws some outOfBoundsException: -1 ... don't touch it.
		// Not anymore ...  but I will leave above comment ... just in case.
		this.mainOptionsMenu.setVisible(visibility);
		this.submenu.setVisible(visibility);
	}

	@Override
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	// @Override
	// public CommandProcessor getCommandProcessor() {
	// 	return this.commandProcessor;
	// }

	@Override
	public Menu getMainOptionsMenu() {
		return this.mainOptionsMenu;
	}

	@Override
	public Menu getSubmenu() {
		return this.submenu;
	}

	@Override
	public FieldDescription getDescriptionMenu() {
		return this.descriptionMenu;
	}

	@Override
	public void setMenuPosition(Point2D position) {
		this.mainOptionsMenu.setPosition(position);
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

		// if (commandProcessor != null && commandProcessor instanceof ActiveComponent) {
		// 	((ActiveComponent) commandProcessor).shutdown();
		// }
		if (commandQueue != null && commandQueue instanceof ActiveComponent) {
			((ActiveComponent) commandQueue).shutdown();
		}

		Platform.runLater(() -> {
			hide();
		});
	}

	@Override
	public ViewConfig getActiveConfig() {
		return this.config;
	}

	@Override
	public void setDescriptionVisibility(boolean visibility) {
		this.descriptionMenu.setVisible(visibility);
	}

	@Override
	public void setDescriptionPosition(Point2D position) {
		this.descriptionMenu.setPosition(position);
	}

	@Override
	public void showCoins(int amount) {
		this.coinsText.setText(formCoinsString(amount));
	}

	@Override
	public void updatePoints(int income, int amount) {
		this.pointsUi.setText(formPointsString(amount));
	}

	private String formCoinsString(int amount) {
		return "" + amount;
	}

	private String formPointsString(int amount) {
		return "Points: " + amount;
	}

	@Override
	public void showWinner(String winner, int amount) {
		this.winnerUi.setText("Winner: " + winner + "\n" + "Bonus: " + amount);
		this.winnerUi.setVisible(true);

		// This "hack" will ensure that no clicks will be handled.
		// TextUiRoot is above the secondLayerCanvas which is collecting/handling
		// clicks.
		this.textUiRoot.setMouseTransparent(false);
	}

}
