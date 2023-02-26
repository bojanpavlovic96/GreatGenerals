package root.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.Point2D;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.event.ViewEventProducer;
import root.view.field.ViewField;
import root.view.menu.FieldDescription;
import root.view.menu.Menu;

public interface View extends ViewEventProducer, CommandDrivenComponent {

	String getViewTheme();

	void showView();

	void hideView();

	void setCanvasVisibility(boolean visibility);

	boolean zoomIn();

	boolean zoomOut();

	ViewField convertToViewField(Field model);

	double getFieldWidth();

	double getFieldHeight();

	double getFieldBorderWidth();

	GraphicsContext getMainGraphicContext();

	GraphicsContext getTopLayerGraphicContext();

	Color getBackgroundColor();

	void setMenuVisibility(boolean visibility);

	void setMenuPosition(Point2D position);

	void setDescriptionVisibility(boolean visibility);

	void setDescriptionPosition(Point2D position);

	Menu getMainOptionsMenu();

	Menu getSubmenu();

	FieldDescription getDescriptionMenu();

	// attention bad approach
	void adjustCanvasSize(Point2D point);

	ViewConfig getActiveConfig();

}
