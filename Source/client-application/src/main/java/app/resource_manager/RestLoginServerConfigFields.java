package app.resource_manager;

public class RestLoginServerConfigFields {

	public String address;
	public String port;
	public String loginPath;
	public String registerPath;

	public RestLoginServerConfigFields() {

	}

	public RestLoginServerConfigFields(
			String address,
			String port,
			String loginPath,
			String registerPath) {

		this.address = address;
		this.port = port;
		this.loginPath = loginPath;
		this.registerPath = registerPath;
	}

}
