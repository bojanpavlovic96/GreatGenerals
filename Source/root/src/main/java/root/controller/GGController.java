package root.controller;

import root.ActiveComponent;
import root.model.Model;
import root.view.GGView;

public interface GGController extends ActiveComponent {

	GGView getView();

	void setView(GGView new_view);

	Model getModel();

	void setModel(Model new_model);

}
