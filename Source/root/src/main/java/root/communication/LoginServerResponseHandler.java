package root.communication;

import root.communication.messages.LoginServerResponse;

public interface LoginServerResponseHandler {
	void handle(LoginServerResponse response);
}
