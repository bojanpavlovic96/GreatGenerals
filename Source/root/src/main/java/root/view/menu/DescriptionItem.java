package root.view.menu;

import java.util.List;

public interface DescriptionItem {

	String getTitle();

	List<String> getTextItems();

	String getIconSource();

	boolean imageAsBackground();

	String toString();

}
