package root.communication;

import root.communication.messages.LoginServerResponse;

public interface LoginServerResponseHandler {
	void handleResponse(LoginServerResponse response);
}
