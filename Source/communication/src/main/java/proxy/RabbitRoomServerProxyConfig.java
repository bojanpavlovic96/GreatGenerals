package proxy;

public class RabbitRoomServerProxyConfig {

	public String rabbitTopicExchangeKeyword;

	public String newRoomRequestExchange;
	public String newRoomRequestRoutePrefix;

	public String joinRoomRequestExchange;
	public String joinRoomRequestRoutePrefix;

	public String roomResponseExchange;
	public String roomResponseRoutePrefix;

	public RabbitRoomServerProxyConfig() {

	}

}
