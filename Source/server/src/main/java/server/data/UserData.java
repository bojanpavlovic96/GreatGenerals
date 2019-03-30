package server.data;

public class UserData {

	private String username;
	private String password;
	private int score;

	public UserData(String username, String password, int score) {
		super();
		this.username = username;
		this.password = password;
		this.score = score;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
