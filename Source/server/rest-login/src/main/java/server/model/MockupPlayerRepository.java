package server.model;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
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

}
