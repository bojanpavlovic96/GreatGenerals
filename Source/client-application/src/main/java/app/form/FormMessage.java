package app.form;

public class FormMessage {

	private String message;

	private String color;

	public FormMessage() {
		super();
	}

	public FormMessage(String message, String color) {
		super();
		this.message = message;
		this.color = color;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
