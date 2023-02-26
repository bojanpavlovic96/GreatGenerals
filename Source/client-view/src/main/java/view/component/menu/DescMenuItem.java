package view.component.menu;

import java.util.List;

import root.communication.parser.StaticParser;
import root.view.menu.DescriptionItem;

public  class DescMenuItem implements DescriptionItem {

	private String title;
	private List<String> items;
	private String iconName;
	private boolean asBackground;

	public DescMenuItem(String title, List<String> items, String iconName, boolean asBackground) {

		this.title = title;
		this.items = items;
		this.iconName = iconName;
		this.asBackground = asBackground;

	}

	public DescMenuItem(String title, List<String> items, String iconName) {
		this(title, items, iconName, false);
	}

	@Override
	public List<String> getTextItems() {
		return items;
	}

	@Override
	public String getIconSource() {
		return iconName;
	}

	@Override
	public boolean imageAsBackground() {
		return asBackground;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String toString(){
		return StaticParser.ToString(this);

	}

}
