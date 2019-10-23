package view.component.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import root.model.component.option.FieldOption;
import root.view.menu.MenuItem;

public class Option extends Button implements MenuItem {

	private String name;

	private Runnable on_click;

	public Option(FieldOption option) {
		super(option.getName());

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
	public void setName(String item_name) {
		this.name = item_name;
	}

	@Override
	public Runnable getOnClickRunnable() {
		return on_click;
	}

	@Override
	public void setOnClickRunnable(Runnable new_runnable) {
		this.on_click = new_runnable;

		// method from button
		super.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// debug
				System.out.println("On click event from menu option ... :");

				if (on_click != null) {
					on_click.run();
				}

			}
		});

	}

}
