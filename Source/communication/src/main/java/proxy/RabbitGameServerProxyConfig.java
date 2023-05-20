package proxy;

public class RabbitGameServerProxyConfig {

	public String rabbitTopicExchangeKeyword;

	public String serverMessageExchange;
	public String serverMessageRoutePrefix;
	public String rabbitMatchAllWildcard;

	public String clientIntentionExchange;
	public String clientIntentionRoutePrefix;

	public RabbitGameServerProxyConfig() {

	}

}