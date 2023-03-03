package root.communication;

import root.command.CommandProducer;
import root.model.event.ClientIntention;

public interface GameServerProxy extends CommandProducer {

	boolean sendIntention(ClientIntention action);

	String getUsername();

	String getRoomName();

}
