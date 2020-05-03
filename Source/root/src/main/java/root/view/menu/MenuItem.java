package root.view.menu;

public interface MenuItem {

	String getName();

	Runnable getOnClickRunnable();

	void setOnClickRunnable(Runnable new_runnable);

}
