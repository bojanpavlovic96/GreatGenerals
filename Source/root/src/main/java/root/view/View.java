package root.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.event.ViewEventProducer;
import root.view.field.ViewField;

public interface View extends ViewEventProducer, CommandDrivenComponent {

	String getViewTheme();

	void show();

	void hide();

	void setCanvasVisibility(boolean visibility);

	boolean zoomIn();

	boolean zoomOut();

	ViewField convertToViewField(Field model);

	double getFieldWidth();

	double getFieldHeight();

	double getFieldBorderWidth();

	GraphicsContext getGraphicContext();

	Color getBackgroundColor();

	void showMenu();

	void hideMenu();

	// attention bad approach
	void adjustCanvasSize();

}
