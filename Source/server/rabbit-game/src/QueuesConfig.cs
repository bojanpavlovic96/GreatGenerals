namespace RabbitGameServer
{

	public class QueuesConfig
	{

		public static string ConfigSection = "QueuesConfig";

		public string NewGameTopic { get; set; }
		public string NewGameRoute { get; set; }
		public string ModelEventTopic { get; set; }
		public string ModelEventRoute { get; set; }

		public string ServerCommandTopic { get; set; }
		public string ServerCommandRoutePrefix { get; set; }
	}

}