package proxy;

import root.communication.parser.StaticParser;

public class RestLoginServerConfig {

	private static final Object PROD_STAGE = "prod";
	private static final Object DEV_STAGE = "dev";

	public String stage;

	public RestLoginServerConfigFields production;
	public RestLoginServerConfigFields development;

	public RestLoginServerConfig() {

	}

	public RestLoginServerConfigFields getActive() {
		if (this.stage.equals(PROD_STAGE)) {
			return this.production;
		} else if (this.stage.equals(DEV_STAGE)) {
			return this.development;
		} else {
			System.out.println("Rest login config has wrong stage value ... ");

			return null;
		}
	}

	@Override
	public String toString() {
		return StaticParser.ToString(this.getActive());
	}

}
