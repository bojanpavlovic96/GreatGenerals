namespace RabbitGameServer
{

	public class QueuesConfig
	{

		public static string ConfigSection = "QueuesConfig";

		public string? MatchAllWildcard { get; set; }
		public string? RoomsRequestTopic { get; set; }
		public string? RoomsResponseTopic { get; set; }
		public string? NewGameRoute { get; set; }
		public string? JoinGameRoute { get; set; }
		public string? RoomResponseRoute { get; set; }
		public string? RoomUpdateRoute { get; set; }
		public string? ModelEventTopic { get; set; }
		public string? ModelEventRoute { get; set; }

		// TODO rename this to serverMessageTopic
		public string? ServerCommandTopic { get; set; }
		public string? ServerCommandRoutePrefix { get; set; }
	}

}
