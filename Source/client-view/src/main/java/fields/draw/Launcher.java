package fields.draw;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

	private DrawingStage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated stage

		this.stage = new DrawingStage();

		this.stage.show();

		new Thread(new Runnable() {

			public void run() {

				CommandGenerator generator = new CommandGenerator(stage.getCommandQueue());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Calling generator");
				generator.generateCommand();

			}
		}).start();

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		this.stage.getExecutor().shutdown();
	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
