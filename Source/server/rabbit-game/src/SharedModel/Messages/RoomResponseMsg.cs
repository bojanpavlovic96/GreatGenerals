
namespace RabbitGameServer.SharedModel.Messages
{
	public class RoomResponseMsg : Message
	{
		public RoomResponseType responseType;

		public List<PlayerData> players;

		public RoomResponseMsg(DateTime timestamp,
			RoomResponseType type,
			string player,
			string roomName,
			List<PlayerData> players)
			: base(MessageType.RoomResponse, timestamp, player, roomName)
		{
			this.responseType = type;
			this.players = players;
		}

		public static RoomResponseMsg unknownFail()
		{
			return new RoomResponseMsg(DateTime.Now, RoomResponseType.UnknownFail, null, null, null);
		}

		public static RoomResponseMsg success(string requester,
				string roomName,
				List<PlayerData> players)
		{

			return new RoomResponseMsg(DateTime.Now, RoomResponseType.Success, requester, roomName, players);
		}

	}
}