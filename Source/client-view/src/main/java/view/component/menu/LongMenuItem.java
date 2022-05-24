package view.component.menu;

import root.model.component.option.FieldOption;
import root.view.menu.MenuItem;

// also should extend some javafx class panel, or something ... 
// look at shortMenuOption for reference
public class LongMenuItem implements MenuItem {

	private String name;
	private Runnable onClick;

	public LongMenuItem(FieldOption option) {

		this.name = option.getName();
		this.setOnClickHandler(onClick);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Runnable getOnClickHandler() {
		return this.onClick;
	}

	@Override
	public void setOnClickHandler(Runnable onClick) {
		this.onClick = onClick;
	}

}
