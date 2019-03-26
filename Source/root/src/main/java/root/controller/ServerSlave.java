package root.controller;

import root.communication.ServerProxy;

public interface ServerSlave {

	ServerProxy getServerProxy();

	void setServerProxy(ServerProxy new_proxy);

}
