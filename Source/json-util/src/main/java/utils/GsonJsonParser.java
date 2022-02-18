package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class GsonJsonParser implements JsonParser {

	// public static String ToString(Object obj) {
	// var builder = new GsonBuilder();
	// var gson = builder.create();

	// return gson.toJson(obj);
	// }

	// public static <T> T FromString(String str, Class<T> type) {
	// var builder = new GsonBuilder();
	// var gson = builder.create();

	// T result = null;
	// try {
	// result = gson.fromJson(str, type);
	// } catch (JsonSyntaxException e) {
	// System.out.println("Failed to parse json from string -> "
	// + e.getMessage());
	// }

	// return result;
	// }

	private Gson gson;

	public GsonJsonParser() {
		var builder = new GsonBuilder();
		builder.setPrettyPrinting();
		this.gson = builder.create();
	}

	@Override
	public String ToString(Object obj) {
		// if (this.gson == null) {
		// 	var builder = new GsonBuilder();
		// 	this.gson = builder.create();
		// }

		return this.gson.toJson(obj);
	}

	@Override
	public <T> T FromString(String str, Class<T> type) {
		// if (this.gson == null) {
		// 	var builder = new GsonBuilder();
		// 	this.gson = builder.create();
		// }

		T result = null;
		try {
			result = this.gson.fromJson(str, type);
		} catch (JsonSyntaxException e) {
			System.out.println("Failed to parse json from string -> "
					+ e.getMessage());
		}

		return result;
	}

}
