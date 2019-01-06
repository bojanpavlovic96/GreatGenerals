package controller;

import model.Model;
import view.View;

public interface Controller extends CanCommunicate {

	View getView();

	void setView(View view);

	Model getModel();

	void setModel(Model model);

}
