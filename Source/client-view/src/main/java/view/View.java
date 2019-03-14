package view;

import model.component.field.Field;
import view.component.ViewField;

public interface View extends EventDrivenComponent, CommandDrivenComponent, ShouldBeShutdown, LayeredView {

	// used just to group all this interfaces into one
	// and define single view interface

	// type represents theme
	String getViewType();

	void show(); // access to stage.show()

	void hide(); // access to stage.hide()

	void setCanvasVisibility(boolean visibility);

	boolean zoomIn();

	boolean zoomOut();

	ViewField convertToViewField(Field model);

	double getFieldHeight();

	double getFieldWidth();

	double getFieldBorderWidth();

	void adjustCanvasSize(ViewField field);

	void singleAdjust(Field width_model, Field height_model);

}
