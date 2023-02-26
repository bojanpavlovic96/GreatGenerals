package root.model.event;

// TODO this is very bad name 
// this events won't be only produced by the model 
public abstract class ModelEventArg {

	public static enum ModelEventType {
		Move,
		Attack,
		Defend,
		ReadyForInit
	}

	private ModelEventType type;

	private String playerName;

	public ModelEventArg(ModelEventType type, String playerName) {
		this.type = type;
		this.setPlayerName(playerName);
	}

	public ModelEventType getEventType() {
		return type;
	}

	public void setEventType(ModelEventType type) {
		this.type = type;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
