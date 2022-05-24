package root.view.menu;

public interface MenuItem {

	String getName();

	Runnable getOnClickHandler();

	void setOnClickHandler(Runnable onClick);

}
