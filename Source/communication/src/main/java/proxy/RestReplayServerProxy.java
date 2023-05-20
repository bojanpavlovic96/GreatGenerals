package proxy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import java.util.concurrent.ExecutorService;

import root.ActiveComponent;
import root.communication.ReplayRequestHandler;
import root.communication.ReplayServerProxy;
import root.communication.messages.ReplayResponseStatus;
import root.communication.messages.ReplayServerResponse;
import root.communication.parser.DataParser;

public class RestReplayServerProxy implements ReplayServerProxy, ActiveComponent {

	private RestReplayProxyConfigFields config;
	private DataParser parser;

	private HttpClient httpClient;

	public RestReplayServerProxy(RestReplayProxyConfigFields config, DataParser parser) {
		this.config = config;
		this.parser = parser;

		this.httpClient = HttpClient.newBuilder().build();
	}

	@Override
	public void listReplays(String username, ReplayRequestHandler handler) {
		var strUri = String.format("http://%s:%d/%s/%s",
				config.address,
				config.port,
				config.listGamesPath,
				username);

		System.out.println("Requesting replays on : " + strUri);

		var uri = URI.create(strUri);

		var request = HttpRequest.newBuilder(uri)
				.GET()
				.build();

		httpClient
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::responseParser)
				.thenAccept(handler::handle)
				.exceptionally((Throwable t) -> {
					System.out.println("Exception while handling replay proxy response ... ");
					System.out.println(t.getMessage());

					handler.handle(new ReplayServerResponse(ReplayResponseStatus.SERVER_ERROR));
					return null;
				})
				.join();
	}

	private ReplayServerResponse responseParser(String strObj) {
		System.out.println("Replay server response: ");
		System.out.println(strObj);
		return parser.FromString(strObj, ReplayServerResponse.class);
	}

	@Override
	public void shutdown() {
		if (httpClient != null && httpClient.executor().isPresent()) {
			System.out.println("Tried to shutdown thread pool inside the httpClient but ... yeah ... (replayProxy) ");
			((ExecutorService) httpClient.executor().get()).shutdownNow();
		}
	}

	@Override
	public void loadReplay(String roomId, ReplayRequestHandler handler) {
		var strUri = String.format("http://%s:%d/%s/%s",
				config.address,
				config.port,
				config.loadGamePath,
				roomId);

		System.out.println("Requesting game load on : " + strUri);

		var uri = URI.create(strUri);

		var request = HttpRequest.newBuilder(uri)
				.GET()
				.build();

		httpClient
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::responseParser)
				.thenAccept(handler::handle)
				.exceptionally((Throwable t) -> {
					System.out.println("Exception while handling replay proxy response ... ");
					System.out.println(t.getMessage());

					handler.handle(new ReplayServerResponse(ReplayResponseStatus.SERVER_ERROR));
					return null;
				})
				.join();
	}

}
