package view.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.canvas.GraphicsContext;

import root.Point2D;

import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;

import root.view.View;
import root.view.field.ViewField;
import root.view.menu.DescriptionItem;
import root.view.menu.Menu;
import view.component.menu.DescMenuItem;

public class ShowFieldMenuCommand extends Command {

	public static double INFO_WIDTH = 400;
	public static double INFO_HEIGHT = 200;

	private Field selectedField;

	private Field targetField;
	private ViewField viewTarget;

	public ShowFieldMenuCommand(Field selectedField, Field targetField) {

		this.selectedField = selectedField;
		this.targetField = targetField;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewTarget = ((View) super.targetComponent)
				.convertToViewField(this.targetField);

		if (this.selectedField == null) {
			this.selectedField = this.targetField;
		}

	}

	@Override
	public void run() {

		View view = (View) targetComponent;

		Menu menu = view.getMainOptionsMenu();
		menu.clearOptions();

		selectedField.adjustOptionsFor(targetField);
		menu.populateWith(selectedField.getEnabledOptions());

		view.setMenuPosition(new Point2D(
				viewTarget.getFieldCenter().getX(),
				viewTarget.getFieldCenter().getY()));

		menu.setVisible(true);
		// view.setMenuVisibility(true);

		// draw field info

		GraphicsContext gc = ((View) targetComponent).getTopLayerGraphicContext();
		gc.save();

		var descMenu = view.getDescriptionMenu();
		var fieldDesc = viewTarget.getDescription();

		System.out.println("Descriptions ... ");
		for (var desc : fieldDesc) {
			System.out.println(desc);
		}

		descMenu.populateWith(fieldDesc);

		var descPosition = new Point2D(
				menu.getPosition().x + menu.getMenuWidth(),
				menu.getPosition().y);
		descMenu.setPosition(descPosition);

		// descMenu.setPosition(new Point2D(100, 200));
		System.out.println("Showing description menu .. ");
		descMenu.setVisible(true);

	}

	@Override
	public Command getAntiCommand() {
		return new ClearTopLayerCommand();
	}

}