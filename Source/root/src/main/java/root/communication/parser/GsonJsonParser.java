package root.communication.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class GsonJsonParser implements DataParser {

	private Gson gson;

	public GsonJsonParser() {
		var builder = new GsonBuilder();
		builder.setPrettyPrinting();
		this.gson = builder.create();
	}

	@Override
	public String ToString(Object obj) {
		return this.gson.toJson(obj);
	}

	@Override
	public byte[] toByte(Object obj) {
		return ToString(obj).getBytes();
	}

	@Override
	public <T> T FromString(String str, Class<T> type) {
		T result = null;
		try {
			result = this.gson.fromJson(str, type);
		} catch (JsonSyntaxException e) {
			System.out.println("Failed to parse json from string -> "
					+ e.getMessage());
		}

		return result;
	}

	@Override
	public <T> T FromByte(byte[] bytes, Class<T> type) {
		return FromString(new String(bytes), type);
	}

}
