package model.event;

public abstract class ModelEventArg {

	// translator uses this field to determine how to translate message to
	// appropriate format
	private String event_name;

	// constructors

	public ModelEventArg(String event_name) {
		this.setEventName(event_name);
	}

	// getters and setters

	public String getName() {
		return event_name;
	}

	public void setEventName(String event_name) {
		this.event_name = event_name;
	}

}
