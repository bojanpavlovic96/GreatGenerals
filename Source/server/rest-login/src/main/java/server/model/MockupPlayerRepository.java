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
	public void updatePoints(String username, int points) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'udpate'");
	}

	// @Override
	// public Player updatePoinst(String username, int points) {
	// 	// TODO Auto-generated method stub
	// 	throw new UnsupportedOperationException("Unimplemented method 'updatePoinst'");
	// }

}
