package view.component.menu;

import javafx.scene.control.Button;
import root.view.menu.MenuItem;

public class Option extends Button implements MenuItem {

	private String name;

	public Option(String name) {
		super(name);

		this.name = name;

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String item_name) {
		this.name = item_name;
	}

}
