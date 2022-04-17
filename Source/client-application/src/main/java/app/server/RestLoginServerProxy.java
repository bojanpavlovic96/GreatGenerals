package app.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import app.resource_manager.RestLoginServerConfigFields;
import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.RegisterRequest;
import root.communication.parser.DataParser;

public class RestLoginServerProxy implements LoginServerProxy {

	private RestLoginServerConfigFields config;
	private DataParser jsonParser;

	public RestLoginServerProxy(
			RestLoginServerConfigFields config,
			DataParser jsonParser) {

		this.config = config;
		this.jsonParser = jsonParser;
	}

	@Override
	public void login(LoginRequest requestObj, LoginServerResponseHandler handler) {

		String strUri = String.format("https://%s:%d/%s",
				config.address,
				config.port,
				config.loginPath);

		URI uri = URI.create(strUri);
		// String payload = "{\"name\":\"some_name\",\"password\":\"some_password\"}";

		// TODO replace with actuall passed request
		var mockupRequest = new LoginRequest("some_name", "some_password");
		var sPayload = jsonParser.ToString(mockupRequest);

		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(sPayload))
				.build();

		HttpClient.newHttpClient()
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::responseParser)
				.thenAccept(handler::handle)
				.join();

	}

	@Override
	public void register(RegisterRequest requestObj, LoginServerResponseHandler handler) {
		String strUri = String.format("https://%s:%d/%s",
				config.address,
				config.port,
				config.registerPath);

		URI uri = URI.create(strUri);
		var sPayload = jsonParser.ToString(requestObj);

		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(sPayload))
				.build();

		HttpClient.newHttpClient()
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::responseParser)
				.thenAccept(handler::handle)
				.join();
	}

	private LoginServerResponse responseParser(String strObj) {
		return jsonParser.FromString(strObj, LoginServerResponse.class);
	}

	@Override
	public boolean isReady() {
		// if this serverProxy was implemented using message broker waiting for it
		// to create necessary channels and topics would require this method,
		// in this implementation (rest) it is always ready to send requests
		return true;
	}

}
