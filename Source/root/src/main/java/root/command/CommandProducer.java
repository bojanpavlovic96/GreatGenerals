package root.command;

public interface CommandProducer {

	CommandQueue getConsumerQueue();

	void setConsumer(CommandQueue queue);

}
