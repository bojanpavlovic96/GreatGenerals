package root.communication;

public interface ReplayServerProxy {
	void listReplays(String username, ReplayRequestHandler handler);
}
