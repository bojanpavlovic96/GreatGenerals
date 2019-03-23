package root.command;

public interface CommandProducer {

	void registerConsumerQueue(CommandQueue consumer_queue);

	// attention this should be list of command queues but ...
	CommandQueue getCommandConsumer();

}
