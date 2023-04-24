package proxy;

public class RestReplayProxyConfigFields {

	public String address;
	public int port;
	public String listGamesPath;

	public RestReplayProxyConfigFields() {

	}

	public RestReplayProxyConfigFields(String address, int port, String listGamesPath) {
		this.address = address;
		this.port = port;
		this.listGamesPath = listGamesPath;
	}

}
