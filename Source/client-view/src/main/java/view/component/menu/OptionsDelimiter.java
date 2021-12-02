package view.component.menu;

import javafx.scene.control.Button;
import root.view.menu.MenuItem;

public class OptionsDelimiter extends Button implements MenuItem {

	// TODO add some special style

	private String label;

	public OptionsDelimiter(String label) {
		this.label = label;
	}

	@Override
	public String getName() {
		return this.label;
	}

	@Override
	public Runnable getOnClickRunnable() {
		return null;
	}

	@Override
	public void setOnClickRunnable(Runnable new_runnable) {

	}

}
