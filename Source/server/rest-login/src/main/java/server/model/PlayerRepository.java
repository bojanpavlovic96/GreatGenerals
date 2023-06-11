package server.model;

public interface PlayerRepository {

	Player getByName(String username);

	Player save(Player player);

	int updatePoints(String username, int points);

}
