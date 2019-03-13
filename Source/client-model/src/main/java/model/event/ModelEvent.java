package model.event;

public abstract class ModelEvent {

	// translator uses this field to determine how to translate message to
	// appropriate format
	private String event_name;

	// constructors

	public ModelEvent(String event_name) {
		this.setEventName(event_name);
	}

	// getters and setters

	public String getEventName() {
		return event_name;
	}

	public void setEventName(String event_name) {
		this.event_name = event_name;
	}

}
