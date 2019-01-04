package view.command;

import javafx.scene.canvas.Canvas;

public abstract class ViewCommand implements Runnable {

	protected Canvas canvas;

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

}
