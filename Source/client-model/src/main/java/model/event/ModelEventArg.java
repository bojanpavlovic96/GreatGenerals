package model.event;

public abstract class ModelEventArg {

	// translator uses this field to determine how to translate message to
	// appropriate format
	private String event_name;

	private String player_name;

	// constructors

	public ModelEventArg(String event_name, String player_name) {
		this.event_name = event_name;
		this.setPlayerName(player_name);
	}

	// getters and setters

	public String getEventName() {
		return event_name;
	}

	public void setEventName(String event_name) {
		this.event_name = event_name;
	}

	public String getPlayerName() {
		return player_name;
	}

	public void setPlayerName(String player_name) {
		this.player_name = player_name;
	}

}
