package fields.draw;

import fields.command.ClearHex;
import fields.command.CommandQueue;
import fields.command.HexagonDrawer;
import fields.command.ViewCommand;
import javafx.geometry.Point2D;

public class CommandGenerator {

	private CommandQueue queue;

	public CommandGenerator(CommandQueue queue) {
		this.queue = queue;
	}

	public void generateCommand() {

		Hexagon start_hex = new Hexagon(new Point2D(0, 0), new Point2D(200, 200), 50);

		System.out.println("Ready to enqueue command ...");
		queue.enqueue(new HexagonDrawer(start_hex));
		System.out.println("Done equeueing ...");
		queue.enqueue(new HexagonDrawer(start_hex.getNextOnX()));
		queue.enqueue(new HexagonDrawer(start_hex.getNextOnY()));
		queue.enqueue(new HexagonDrawer(start_hex.getPrevtOnX()));
		queue.enqueue(new HexagonDrawer(start_hex.getPrevOnY()));
		queue.enqueue(new HexagonDrawer(start_hex.getNextOnXY()));
		queue.enqueue(new HexagonDrawer(start_hex.getPrevOnXY()));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		queue.enqueue(new ClearHex(start_hex));
		queue.enqueue(new HexagonDrawer(start_hex));
	}

}
