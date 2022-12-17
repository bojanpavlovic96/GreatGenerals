package proxy;

public class RabbitGameServerProxyConfig {

	public String rabbitTopicExchangeKeyword;

	public String serverMessageExchange;
	public String serverMessageRoutePrefix;
	public String rabbitMatchAllWildcard;

	public String modelEventsExchange;
	public String modelEventsRoutePrefix;

	public RabbitGameServerProxyConfig() {

	}

}