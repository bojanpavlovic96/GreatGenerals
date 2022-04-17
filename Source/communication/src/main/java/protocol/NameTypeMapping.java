package protocol;

public class NameTypeMapping {
	public String name;
	public Class<?> type;

	public NameTypeMapping(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

}