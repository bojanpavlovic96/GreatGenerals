package controller;

import model.Model;
import view.ShouldBeShutdown;
import view.View;

public interface Controller extends CanCommunicate, ShouldBeShutdown {

	View getView();

	void setView(View view);

	Model getModel();

	void setModel(Model model);

}
