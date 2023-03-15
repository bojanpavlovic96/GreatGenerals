package root.controller;

import root.communication.GameServerProxy;

public interface ServerSlave {

	GameServerProxy getServerProxy();

	void setServerProxy(GameServerProxy newProxy);

}
