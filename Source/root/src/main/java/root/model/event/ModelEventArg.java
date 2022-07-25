package root.model.event;

public abstract class ModelEventArg {

	public static enum EventType {
		MoveModelEvent,
		AttackModelEvent
	}

	private EventType type;

	private String playerName;

	public ModelEventArg(EventType type, String playerName) {
		this.type = type;
		this.setPlayerName(playerName);
	}

	public EventType getEventName() {
		return type;
	}

	public void setEventName(EventType type) {
		this.type = type;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
