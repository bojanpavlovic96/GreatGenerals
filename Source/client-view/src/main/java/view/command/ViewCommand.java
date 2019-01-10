package view.command;

import model.component.field.Field;
import view.LayeredView;
import view.component.Hexagon;

public abstract class ViewCommand implements Runnable {

	protected LayeredView view;
	protected Hexagon hex;

	public ViewCommand() {

	}

	public ViewCommand(LayeredView view, Field model) {
		this(model);

		this.view = view;

	}

	public ViewCommand(Field model) {

		this.hex = new Hexagon(model, DrawFieldCommand.default_hex_size, DrawFieldCommand.default_border_width);

	}

	public ViewCommand(Hexagon hex) {
		this.hex = hex;
	}

	public ViewCommand(LayeredView view) {
		this.view = view;
	}

	public void setView(LayeredView view) {
		this.view = view;
	}

	public LayeredView getView() {
		return this.view;
	}

	public Hexagon getHex() {
		return hex;
	}

	public void setHex(Hexagon hex) {
		this.hex = hex;
	}

}
