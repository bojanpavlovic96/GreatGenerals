package view;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import view.command.ClearHex;
import view.command.CommandQueue;
import view.command.FilterSetter;
import view.command.DrawHexagonCommand;
import view.component.Hexagon;

// testing purpose

public class CommandGenerator {

	private CommandQueue queue;

	public CommandGenerator(CommandQueue queue) {
		this.queue = queue;
	}

	public void generateCommand() {

		Hexagon hex0 = new Hexagon(new Point2D(0, 0), 50);
		Hexagon hex1 = new Hexagon(new Point2D(1, 0), 50);
		Hexagon hex2 = new Hexagon(new Point2D(2, 0), 50);

		Hexagon hex3 = new Hexagon(new Point2D(3, 0), 50);
		Hexagon hex4 = new Hexagon(new Point2D(4, -1), 50);

		queue.enqueue(new DrawHexagonCommand(hex0));
		queue.enqueue(new DrawHexagonCommand(hex1));
		queue.enqueue(new DrawHexagonCommand(hex2));
		queue.enqueue(new DrawHexagonCommand(hex3));
		queue.enqueue(new DrawHexagonCommand(hex4));

		// System.out.println("Ready to enqueue command ...");
		// queue.enqueue(new DrawHexagonCommand(start_hex));
		// System.out.println("Done equeueing ...");
		// queue.enqueue(new DrawHexagonCommand(start_hex.getNextOnX()));
		// queue.enqueue(new DrawHexagonCommand(start_hex.getNextOnY()));
		// queue.enqueue(new DrawHexagonCommand(start_hex.getPrevtOnX()));
		// queue.enqueue(new DrawHexagonCommand(start_hex.getPrevOnY()));
		// queue.enqueue(new DrawHexagonCommand(start_hex.getNextOnXY()));
		// queue.enqueue(new DrawHexagonCommand(start_hex.getPrevOnXY()));

		queue.enqueue(new FilterSetter(Color.rgb(1, 1, 1), hex1));

	}

	public void drawHex(Point2D point) {

		Hexagon fake_hex = new Hexagon(point);

		queue.enqueue(new DrawHexagonCommand(fake_hex));

	}

}
