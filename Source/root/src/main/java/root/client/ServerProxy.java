package root.client;

import root.ActiveComponent;

public interface ServerProxy extends ActiveComponent, CommandProducer {

	// TODO add arguments (intention - model action )
	void sendIntention();

}
