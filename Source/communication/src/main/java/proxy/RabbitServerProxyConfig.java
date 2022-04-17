package proxy;

public class RabbitServerProxyConfig {

	public String rabbitTopicExchangeKeyword;

	public String serverCommandsExchange;
	public String serverCommandsQueue;
	public String serverCommandsRoutePrefix;

	public String clientEventsExchange;
	public String clientEventsQueue;
	public String clientEventsRoutePrefix;

	public RabbitServerProxyConfig() {

	}

}