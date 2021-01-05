package root.communication.messages;

public class LoginServerResponse {

	private String username;
	private String level;
	private int points;

	private LoginServerResponseStatus status;
	private String message;

	public LoginServerResponse() {
	}

	public LoginServerResponse(String username,
			String level,
			int points,
			LoginServerResponseStatus status,
			String message) {
		this.username = username;
		this.level = level;
		this.points = points;
		this.status = status;
		this.setMessage(message);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public LoginServerResponseStatus getStatus() {
		return this.status;
	}

	public void setStatus(LoginServerResponseStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "{" +
				" username='" + getUsername() + "'" +
				", level='" + getLevel() + "'" +
				", points='" + getPoints() + "'" +
				", status='" + getStatus() + "'" +
				"}";
	}

}
