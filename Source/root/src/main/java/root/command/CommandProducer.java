package root.command;

public interface CommandProducer {

	// TODO this should be removed since this is command PRODUCER
	// others should listening to his queue 
	void setConsumerQueue(CommandQueue consumer_queue);

	CommandQueue getConsumerQueue();

}
