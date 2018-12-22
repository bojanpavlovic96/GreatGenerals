package rabbit.api;

import java.util.List;

import actions.Move;
import board.Field;
import figures.Figure;

public interface Messenger {

	/**
	 * 
	 * @param move
	 * @param userIds
	 * 
	 *            broadcast move to users listed in userIds list. only users that
	 *            can see field from move get notification about figure movement.
	 */
	void broadcastMove(Move move, List<Integer> userIds);

	/**
	 * 
	 * @param units
	 * @return
	 * 
	 * 		used to initialize board in the beginning of the game return list of
	 *         fields visible to the player
	 */
	List<Field> getBoardFields(List<Figure> units);

	
	
	
}
