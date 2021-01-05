package app.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.RegisterRequest;

public class RestLoginServerProxy implements LoginServerProxy {

	public RestLoginServerProxy() {

	}

	@Override
	public void login(LoginRequest requestObj, LoginServerResponseHandler handler) {

		URI uri = URI.create("http://127.0.0.1:8080/login");
		String payload = "{\"name\":\"some_name\",\"password\":\"some_password\"}";

		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(payload))
				.build();

		HttpClient.newHttpClient()
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(RestLoginServerProxy::responsePareser)
				.thenAccept(handler::handleResponse)
				.join();

	}

	@Override
	public void register(RegisterRequest request, LoginServerResponseHandler handler) {
		// TODO Auto-generated method stub

	}

	private static LoginServerResponse responsePareser(String strObj) {

		return null;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

}
