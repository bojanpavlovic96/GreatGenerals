package view;

public interface View
		extends EventDrivenComponent, CommandDrivenComponent, ShouldBeShutdown, LayeredView {

	// used just to group all this interfaces into one
	// and define single view interface

	void show(); // access to stage.show()

}
