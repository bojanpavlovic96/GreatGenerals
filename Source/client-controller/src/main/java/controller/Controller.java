package controller;

import view.EventDrivenComponent;

public interface Controller {

	EventDrivenComponent getView();

	void setView(EventDrivenComponent view);

	
	
}
