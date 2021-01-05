package app.server;

import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;

public class MockupLoginServerProxy implements LoginServerProxy {

	public MockupLoginServerProxy() {

	}

	@Override
	public void login(LoginRequest request, LoginServerResponseHandler handler) {
		System.out.println("MockupServer used for returning login response  ... ");
		System.out.println("Requesting login: " + request.toString());

		handler.handleResponse(new LoginServerResponse(
				request.getUsername(),
				"expert",
				200,
				LoginServerResponseStatus.SUCCESS,
				""));
	}

	@Override
	public void register(RegisterRequest request, LoginServerResponseHandler handler) {
		System.out.println("MockupServer used for returning register response  ... ");
		System.out.println("Requesting registration: " + request.toString());

		handler.handleResponse(new LoginServerResponse(
				request.getUsername(),
				"expert",
				200,
				LoginServerResponseStatus.SUCCESS,
				""));
	}

	@Override
	public boolean isReady() {
		return true;
	}

}
