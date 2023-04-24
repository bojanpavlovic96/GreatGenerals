package proxy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import root.ActiveComponent;
import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.RegisterRequest;
import root.communication.parser.DataParser;

// next argument (added after mvn exec:java) will destory all remaining threads
// after application is shutdown (all windows are closed)
// -Dexec.cleanupDaemonThreads=false

// ^^^ not used but could/should be ... 

public class RestLoginServerProxy implements LoginServerProxy, ActiveComponent {

	private RestLoginServerConfigFields config;
	private DataParser jsonParser;

	private HttpClient httpClient;

	public RestLoginServerProxy(RestLoginServerConfigFields config,
			DataParser dataParser) {

		this.config = config;
		this.jsonParser = dataParser;

		var clientBuilder = HttpClient
				.newBuilder()
				.executor(Executors.newCachedThreadPool());

		httpClient = clientBuilder.build();

	}

	@Override
	public void login(LoginRequest requestObj, LoginServerResponseHandler handler) {

		String strUri = String.format("http://%s:%d/%s",
				config.address,
				config.port,
				config.loginPath);

		var uri = URI.create(strUri);
		var sPayload = jsonParser.ToString(requestObj);

		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(sPayload))
				.build();

		// NOTE this thing will create internal thread pool
		// and hold it even after app is closed (all windows are closed)
		// workaround is to create my own ThreadPoolExecutor and pass it to the HttpClient
		// that way a can destory it manually when application is shutting down 
		// lovely 
		// https: //stackoverflow.com/questions/65333866/java-httpclient-not-releasing-threads

		// HttpClient.newHttpClient()
		httpClient
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::responseParser)
				.thenAccept(handler::handle)
				.exceptionally((Throwable t) -> {
					System.out.println("Got and exception while sending/handling login request ... ");
					System.out.println("Exc: " + t.getMessage());

					handler.handle(LoginServerResponse.failed());

					return null;
				})
				.join();
	}

	@Override
	public void register(RegisterRequest requestObj, LoginServerResponseHandler handler) {
		String strUri = String.format("http://%s:%d/%s",
				config.address,
				config.port,
				config.registerPath);

		URI uri = URI.create(strUri);
		var sPayload = jsonParser.ToString(requestObj);

		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(sPayload))
				.build();

		// HttpClient.newHttpClient()
		httpClient
				.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::responseParser)
				.thenAccept(handler::handle)
				.exceptionally((Throwable t) -> {
					System.out.println("Got and exception while sending/handling"
							+ " register request/response ... ");
					System.out.println("Exc: " + t.getMessage());

					handler.handle(LoginServerResponse.failed());

					return null;
				})
				.join();
	}

	private LoginServerResponse responseParser(String strObj) {
		return jsonParser.FromString(strObj, LoginServerResponse.class);
	}

	@Override
	public boolean isReady() {
		// if this serverProxy was implemented using message broker waiting for it
		// to create necessary channels and topics would require this method,
		// in this implementation (rest) it is always ready to send requests
		return true;
	}

	@Override
	public void shutdown() {
		if (httpClient != null && httpClient.executor().isPresent()) {
			System.out.println("Tried to shutdown thread pool inside the httpClient but ... yeah ... (loginProxy)");
			((ExecutorService) httpClient.executor().get()).shutdownNow();
		}
	}

}
