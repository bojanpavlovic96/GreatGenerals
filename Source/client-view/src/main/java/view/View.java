package view;

public interface View
		extends EventDrivenComponent, CommandDrivenComponent, ShouldBeShutdown, LayeredView {

	// used just to group all this interfaces into one
	// and define single view interface

	// type represents theme
	String getViewType();
	
	void show(); // access to stage.show()
	
}
