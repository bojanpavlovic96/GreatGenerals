package server;

public class Launcher {

	private static String password = "lzMIQ5SJvR083poynUF6Rc8T_QPNUJow";
	private static String username = "wdvozwsr";
	private static String hostname = "raven.rmq.cloudamqp.com";
	private static String uri = "amqp://" + username + ":" + password + "@" + hostname + "/" + username;

	public static void main(String[] args) {

		Server server = new Server(Launcher.uri);

		System.out.println("Launcher done ... ");

	}

}
