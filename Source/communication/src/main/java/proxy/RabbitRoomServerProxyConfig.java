package proxy;

public class RabbitRoomServerProxyConfig {

	public String rabbitMatchAllWildcard;
	public String rabbitTopicExchangeKeyword;

	public String newRoomRequestExchange;
	public String newRoomRequestRoutePrefix;

	public String joinRoomRequestExchange;
	public String joinRoomRequestRoutePrefix;

	public String leaveRoomRequestExchange;
	public String leaveRoomRequestRoutePrefix;

	public String roomResponseExchange;
	public String roomResponseRoutePrefix;

	public String roomUpdateRoutePrefix;

	public RabbitRoomServerProxyConfig() {

	}

}
