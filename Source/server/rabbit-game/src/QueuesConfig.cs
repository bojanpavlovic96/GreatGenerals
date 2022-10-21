namespace RabbitGameServer
{

	public class QueuesConfig
	{

		public static string ConfigSection = "QueuesConfig";

		public string? MatchAllWildcard { get; set; }
		public string? RoomsRequestTopic { get; set; }
		public string? RoomsResponseTopic { get; set; }
		public string? NewRoomRoute { get; set; }
		public string? JoinRoomRoute { get; set; }
		public string? LeaveRoomRoute { get; set; }
		public string? StartGameRoute { get; set; }
		public string? RoomResponseRoute { get; set; }
		public string? RoomUpdateRoute { get; set; }
		public string? ModelEventTopic { get; set; }
		public string? ModelEventRoute { get; set; }

		public string? ServerMessageTopic { get; set; }
		public string? ServerMessageRoutePrefix { get; set; }
	}

}
