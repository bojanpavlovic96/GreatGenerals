package proxy;

public class RabbitServerProxyConfig {

	public String rabbitTopicExchangeKeyword;

	public String serverCommandsExchange;
	public String serverCommandsRoutePrefix;

	public String newGameRequestExchange;
	public String newGameRequestRoutePrefix;

	public String modelEventsExchange;
	public String modelEventsRoutePrefix;

	public RabbitServerProxyConfig() {

	}

}