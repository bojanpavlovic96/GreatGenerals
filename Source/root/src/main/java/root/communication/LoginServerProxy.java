package root.communication;

import root.communication.messages.LoginRequest;
import root.communication.messages.RegisterRequest;

public interface LoginServerProxy {

	void login(LoginRequest request, LoginServerResponseHandler handler);

	void register(RegisterRequest request, LoginServerResponseHandler handler);

	public boolean isReady();

}
