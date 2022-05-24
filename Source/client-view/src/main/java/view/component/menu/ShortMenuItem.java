package view.component.menu;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import root.model.component.option.FieldOption;
import root.view.menu.MenuItem;

public class ShortMenuItem extends Button implements MenuItem {

	private String name;

	private Runnable onClick;

	public ShortMenuItem(FieldOption option) {
		super(option.getName()); // button text

		this.name = option.getName();

		// FieldOption implements runnable
		this.setOnClickHandler(option);

		// TODO add some button style (borders, background color (image) ... )

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Runnable getOnClickHandler() {
		return onClick;
	}

	@Override
	public void setOnClickHandler(Runnable new_runnable) {
		this.onClick = new_runnable;

		// method from button
		super.setOnAction((ActionEvent event) -> {
			// debug
			System.out.println("On click event from menu option ... : " + name);

			if (onClick != null) {
				onClick.run();
			}

		});

	}

}
