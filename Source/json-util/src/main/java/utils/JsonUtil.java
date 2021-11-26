package utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {

	public static String ToString(Object obj) {
		var builder = new GsonBuilder();
		var gson = builder.create();

		return gson.toJson(obj);
	}

	public static <T> T FromString(String str, Class<T> type) {
		var builder = new GsonBuilder();
		var gson = builder.create();

		T result = null;
		try {
			result = gson.fromJson(str, type);
		} catch (JsonSyntaxException e) {
			System.out.println("Failed to parse json from string -> "
					+ e.getMessage());
		}

		return result;
	}

}
