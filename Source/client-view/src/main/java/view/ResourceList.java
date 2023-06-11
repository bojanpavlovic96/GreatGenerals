package view;

public class ResourceList {

	public String name;
	public String extension;
	public String range_start;
	public String range_end;

	public ResourceList() {
	}

	public ResourceList(String name, String extension, String range_start, String range_end) {
		this.name = name;
		this.extension = extension;
		this.range_start = range_start;
		this.range_end = range_end;
	}

}
