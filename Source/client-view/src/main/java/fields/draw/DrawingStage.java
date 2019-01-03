package fields.draw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fields.command.CommandQueue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DrawingStage extends Stage {

	private int STAGE_WIDTH = 800;
	private int STAGE_HEIGHT = 500;

	private Group root;
	private Scene main_scene;
	private Canvas board_canvas;

	private CommandQueue command_queue;
	private ExecutorService executor;
	private CommandProcessor command_processor;

	public DrawingStage() {

		this.initStage();
		this.initCommandQueue();

	}

	private void initStage() {

		this.setWidth(this.STAGE_WIDTH);
		this.setHeight(this.STAGE_HEIGHT);

		this.setResizable(false);
		this.initStyle(StageStyle.UNDECORATED);

		// create root node and add it to the main scene
		this.root = new Group();
		this.main_scene = new Scene(this.root);

		// create canvas and add it to the root node
		this.board_canvas = new Canvas();
		this.root.getChildren().add(this.board_canvas);

		this.board_canvas.setWidth(this.STAGE_WIDTH);
		this.board_canvas.setHeight(this.STAGE_HEIGHT);

		GraphicsContext gc = this.board_canvas.getGraphicsContext2D();

		gc.setFill(Color.GRAY);
		gc.fillRect(0, 0, this.STAGE_WIDTH, this.STAGE_HEIGHT);

		this.setScene(this.main_scene);
	}

	private void initCommandQueue() {

		this.command_queue = new CommandQueue();

		this.setExecutor(Executors.newSingleThreadExecutor());

		this.command_processor = new CommandProcessor(this.getExecutor(), this.board_canvas);

		this.command_queue = new CommandQueue();
		this.command_queue.setOnEnqueueEventHandler(this.command_processor);

	}

	public CommandQueue getCommandQueue() {
		return this.command_queue;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}
