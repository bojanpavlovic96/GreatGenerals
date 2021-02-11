package root.model.event;

public abstract class ModelEventArg {

	// translator uses this field to determine how to translate message to
	// appropriate format
	private String eventName;

	private String playerName;

	// constructors

	public ModelEventArg(String event_name, String player_name) {
		this.eventName = event_name;
		this.setPlayerName(player_name);
	}

	// getters and setters

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String event_name) {
		this.eventName = event_name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String player_name) {
		this.playerName = player_name;
	}

}
