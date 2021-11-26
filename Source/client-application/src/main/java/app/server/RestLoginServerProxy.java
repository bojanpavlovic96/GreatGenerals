package app.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import app.resource_manager.RestLoginServerConfig;
import app.resource_manager.RestLoginServerConfigFields;
import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.RegisterRequest;

public class RestLoginServerProxy implements LoginServerProxy {

	private RestLoginServerConfigFields config;

	public RestLoginServerProxy(RestLoginServerConfigFields config) {
		this.config = config;
	}

	@Override
	public void login(LoginRequest requestObj,
			LoginServerResponseHandler handler) {

		String strUri = String.format("https://%s:%d/%s",
				config.address,
				config.port,
				config.loginPath);

		URI uri = URI.create(strUri);
		String payload = "{\"name\":\"some_name\",\"password\":\"some_password\"}";

		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(payload))
				.build();

		HttpClient.newHttpClient()
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(RestLoginServerProxy::responseParser)
				.thenAccept(handler::handleResponse)
				.join();

	}

	@Override
	public void register(RegisterRequest request, LoginServerResponseHandler handler) {

	}

	private static LoginServerResponse responseParser(String strObj) {

		return null;
	}

	@Override
	public boolean isReady() {
		System.out.println("REST SERVER IS READY IS NOT IMPLEMENTED ... ");
		return false;
	}

}
