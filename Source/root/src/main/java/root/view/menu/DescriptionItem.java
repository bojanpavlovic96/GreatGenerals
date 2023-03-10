package root.view.menu;

import java.util.List;

public class DescriptionItem {

	public String title;
	public List<String> items;
	public String iconSource;
	public boolean asBackground;

	public String actionText;
	public Runnable action;

	public DescriptionItem(String title,
			List<String> items,
			String iconName,
			String actionText,
			Runnable action,
			boolean asBackground) {

		this.title = title;
		this.items = items;
		this.iconSource = iconName;
		this.asBackground = asBackground;
		this.actionText = actionText;
		this.action = action;
	}

	public DescriptionItem(String title,
			List<String> items,
			String iconName) {

		this(title, items, iconName, "", null, false);
	}

	public DescriptionItem(String title,
			List<String> items,
			String iconName,
			String actionName,
			Runnable action) {

		this(title, items, iconName, actionName, action, false);
	}

}
