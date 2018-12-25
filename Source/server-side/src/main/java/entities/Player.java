package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String username;

	private String email;

	private String password;

	private double score;

	@OneToMany
	private List<Game> games;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public Player(long id, String username, String email, String password, double score,
			List<Game> games) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.score = score;
		this.games = games;
	}

	public Player() {

	}

}
