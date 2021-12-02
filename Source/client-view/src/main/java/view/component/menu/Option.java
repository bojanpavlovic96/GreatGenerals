package view.component.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import root.model.component.option.FieldOption;
import root.view.menu.MenuItem;

public class Option extends Button implements MenuItem {

	private String name;

	private Runnable onClick;

	public Option(FieldOption option) {
		super(option.getName()); // button text

		this.name = option.getName();

		// FieldOption implements runnable

		this.setOnClickRunnable(option);

		// TODO add some button style (borders, background color (image) ... )

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Runnable getOnClickRunnable() {
		return onClick;
	}

	@Override
	public void setOnClickRunnable(Runnable new_runnable) {
		this.onClick = new_runnable;

		// method from button
		super.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// debug
				System.out.println("On click event from menu option ... : " + name);

				if (onClick != null) {
					onClick.run();
				}

			}
		});

	}

}
