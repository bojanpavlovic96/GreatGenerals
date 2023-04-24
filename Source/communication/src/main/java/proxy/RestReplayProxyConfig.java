package proxy;

public class RestReplayProxyConfig {
	private static final Object PROD_STAGE = "prod";
	private static final Object DEV_STAGE = "dev";

	public String stage;

	public RestReplayProxyConfigFields development;
	public RestReplayProxyConfigFields production;

	public RestReplayProxyConfigFields getActive() {
		if (stage.equals(PROD_STAGE)) {
			return production;
		} else if (stage.equals(DEV_STAGE)) {
			return development;
		} else {
			System.out.println("Wrong value in stage field in RestReplayProxyConfig ... ");
			return null;
		}
	}

}
