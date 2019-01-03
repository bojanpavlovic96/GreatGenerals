package launcher;

import javafx.application.Application;
import javafx.stage.Stage;

public class View extends Application {

	private DebugStage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.stage = new DebugStage();

		this.stage.show();

		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub

				Controler cont = new Controler(stage);

				System.out.println("Ready to add circle ...");

				cont.addCricle();

			}
		}).start();

	}
	
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		
		this.stage.getExecutor().shutdown();
	
	}
	
	public static void main(String[] args) {

		Application.launch(args);

	}

}
