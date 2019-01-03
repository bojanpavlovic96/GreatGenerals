
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.CommandGenerator;
import view.DrawingStage;
import view.NamedEventHandler;

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

				stage.addEventHandler("mouse-click-event", new NamedEventHandler("testing-mouse-click-event-handler") {

					public void execute(Event arg) {
						System.out.println("mouse-click-event: " + (MouseEvent) arg);
					}
				});

				stage.addEventHandler("key-event-char-k", new NamedEventHandler("testing-key-event-handler-char-k") {

					@Override
					public void execute(Event arg) {

						System.out.println("key-event-char-k" + (KeyEvent) arg);

					}
				});

				System.out.println("Calling generator");
				generator.generateCommand();

			}
		}).start();

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Appliction.Stop() ...");
		this.stage.shutdown();
	}

	public static void main(String[] args) {

		System.out.println("Launching application ...");
		Application.launch(args);
		System.out.println("Application done ...");

	}

}
