package utils;

public interface JsonParser {

	public String ToString(Object obj);

	public <T> T FromString(String str, Class<T> type);

}
