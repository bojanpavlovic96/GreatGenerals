package view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.component.Field;
import model.component.Terrain;
import model.component.Unit;
import view.command.ClearFieldCommand;
import view.command.CommandQueue;
import view.command.SelectFieldCommand;
import view.command.UnselectFieldCommand;
import view.command.DrawFieldCommand;
import view.command.LoadBoardCommand;
import view.component.Hexagon;

// testing purpose

public class CommandGenerator {

	private CommandQueue queue;

	public CommandGenerator(CommandQueue queue) {
		this.queue = queue;
	}

	public void generateCommand() {

		List<Field> models = new ArrayList<Field>();

		for (int i = 5; i < 15; i++) {
			models.add(new Field(new Point2D(i, 1), new Unit(), new Terrain()));
		}

		for (int i = 1; i < 20; i++) {
			models.add(new Field(new Point2D(i, 2), new Unit(), new Terrain()));
		}

		for (int i = 1; i < 20; i++) {
			models.add(new Field(new Point2D(i, 3), new Unit(), new Terrain()));
		}

		for (int i = 3; i < 18; i++) {
			models.add(new Field(new Point2D(i, 4), new Unit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new Field(new Point2D(i, 5), new Unit(), new Terrain()));
		}

		for (int i = -2; i < 18; i++) {
			models.add(new Field(new Point2D(i, 6), new Unit(), new Terrain()));
		}

		for (int i = -4; i < 19; i++) {
			models.add(new Field(new Point2D(i, 7), new Unit(), new Terrain()));
		}

		for (int i = -3; i < 18; i++) {
			models.add(new Field(new Point2D(i, 8), new Unit(), new Terrain()));
		}

		for (int i = -3; i < 17; i++) {
			models.add(new Field(new Point2D(i, 9), new Unit(), new Terrain()));
		}

		for (int i = -4; i < 17; i++) {
			models.add(new Field(new Point2D(i, 10), new Unit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new Field(new Point2D(i, 11), new Unit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new Field(new Point2D(i, 12), new Unit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new Field(new Point2D(i, 13), new Unit(), new Terrain()));
		}

		LoadBoardCommand command = new LoadBoardCommand(models);

		this.queue.enqueue(command);

		this.queue.enqueue(new SelectFieldCommand(models.get(50)));

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.queue.enqueue(new UnselectFieldCommand(models.get(50)));
	}

}
