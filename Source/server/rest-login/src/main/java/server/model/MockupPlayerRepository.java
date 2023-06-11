package server.model;

import org.springframework.stereotype.Repository;

@Repository(value = "MockupPlayerRepository")
public class MockupPlayerRepository implements PlayerRepository {

	@Override
	public Player getByName(String username) {
		return new Player(username, "", 10, 20);
	}

	@Override
	public Player save(Player player) {
		System.out.println("Pretending like I am saving some data ... ");
		return player;
	}

	@Override
	// public Player udpatePoints(Player player) {
	public int updatePoints(String username, int points) {
		return 1;
	}

	// @Override
	// public Player updatePoinst(String username, int points) {
	// 	// TODO Auto-generated method stub
	// 	throw new UnsupportedOperationException("Unimplemented method 'updatePoinst'");
	// }

}
