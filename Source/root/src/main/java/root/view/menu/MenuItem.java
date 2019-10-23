package root.view.menu;

public interface MenuItem {

	String getName();

	void setName(String item_name);

	Runnable getOnClickRunnable();

	void setOnClickRunnable(Runnable new_runnable);

}
