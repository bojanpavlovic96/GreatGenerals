package root.model.event;

public abstract class ClientIntention {

	public static enum ClientIntentionType {
		Move,
		Attack,
		Defend,
		ReadyForInit,
		AbortAttack
	}

	private ClientIntentionType type;

	private String playerName;

	public ClientIntention(ClientIntentionType type, String playerName) {
		this.type = type;
		this.setPlayerName(playerName);
	}

	public ClientIntentionType getEventType() {
		return type;
	}

	public void setEventType(ClientIntentionType type) {
		this.type = type;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
