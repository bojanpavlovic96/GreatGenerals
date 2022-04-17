package root.communication.parser;

public interface DataParser {
	String ToString(Object obj);

	byte[] toByte(Object obj);

	<T> T FromString(String str, Class<T> type);

	<T> T FromByte(byte[] bytes, Class<T> type);
}
