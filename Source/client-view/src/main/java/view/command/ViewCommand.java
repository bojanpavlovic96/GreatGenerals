package view.command;

import model.component.field.Field;
import view.LayeredView;
import view.component.HexagonField;

public abstract class ViewCommand implements Runnable {

	protected LayeredView view;
	protected HexagonField hex;

	public ViewCommand() {

	}

	public ViewCommand(LayeredView view, Field model) {
		this(model);

		this.view = view;

	}

	public ViewCommand(Field model) {

		this.hex = new HexagonField(model, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);

	}

	public ViewCommand(HexagonField hex) {
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

	public HexagonField getHex() {
		return hex;
	}

	public void setHex(HexagonField hex) {
		this.hex = hex;
	}

}
