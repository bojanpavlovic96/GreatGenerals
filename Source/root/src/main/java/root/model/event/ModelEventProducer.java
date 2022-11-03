package root.model.event;

public interface ModelEventProducer {

	// This will likely never get used ...
	// Remove it ... ? 
	ModelEventHandler getModelEventHandler();

	void setModelEventHandler(ModelEventHandler handler);
}
