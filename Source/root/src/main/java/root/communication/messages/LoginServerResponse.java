package root.communication.messages;

public class LoginServerResponse {

	private String username;
	private int level;
	private int points;

	private LoginServerResponseStatus status;

	public LoginServerResponse() {
	}

	private LoginServerResponse(LoginServerResponseStatus status) {
		this.status = status;
	}

	public LoginServerResponse(String username,
			int level,
			int points,
			LoginServerResponseStatus status) {
		this.username = username;
		this.level = level;
		this.points = points;
		this.status = status;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
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

	@Override
	public String toString() {
		return "{" +
				" username='" + getUsername() + "'" +
				", level='" + getLevel() + "'" +
				", points='" + getPoints() + "'" +
				", status='" + getStatus() + "'" +
				"}";
	}

	public static LoginServerResponse failed() {
		return new LoginServerResponse(LoginServerResponseStatus.SERVER_ERROR);
	}

}
