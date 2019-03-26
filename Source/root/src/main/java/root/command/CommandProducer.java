package root.command;

public interface CommandProducer {

	void setConsumerQueue(CommandQueue consumer_queue);

	CommandQueue getConsumerQueue();

}
