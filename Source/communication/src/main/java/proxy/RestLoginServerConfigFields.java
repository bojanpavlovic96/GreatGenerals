package proxy;

public class RestLoginServerConfigFields {

	public String address;
	public int port;
	public String loginPath;
	public String registerPath;

	public RestLoginServerConfigFields() {

	}

	public RestLoginServerConfigFields(
			String address,
			int port,
			String loginPath,
			String registerPath) {

		this.address = address;
		this.port = port;
		this.loginPath = loginPath;
		this.registerPath = registerPath;
	}

}
