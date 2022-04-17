package server.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String password;

	private int level;
	private int points;

	public Player() {
	}

	public Player(String name, String password, int level, int points) {
		this.setLevel(level);
		this.setPoints(points);
		this.setName(name);
		this.setPassword(password);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Player)) {
			return false;
		}

		Player playerObj = (Player) obj;

		return Objects.equals(this.id, playerObj.id)
				&& Objects.equals(this.name, playerObj.name)
				&& Objects.equals(this.password, playerObj.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name, this.password);
	}

	@Override
	public String toString() {
		return String.format("Player:\n"
				+ "Id:%s\n"
				+ "Name:%s\n"
				+ "Password:%s\n",
				this.id,
				this.name,
				this.password);
	}

}
