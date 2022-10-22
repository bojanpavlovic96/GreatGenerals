
namespace RabbitGameServer.SharedModel.Messages
{
	public class RoomResponseMsg : Message
	{
		public RoomResponseType responseType;

		public List<PlayerData> players;

		public RoomResponseMsg(RoomResponseType type,
			string player,
			string roomName,
			List<PlayerData> players)
			: base(MessageType.JoinResponse, player, roomName)
		{
			this.responseType = type;
			this.players = players;
		}

		public static RoomResponseMsg unknownFail()
		{
			return new RoomResponseMsg(RoomResponseType.UnknownFail, null, null, null);
		}

		public static RoomResponseMsg success(string requester,
				string roomName,
				List<PlayerData> players)
		{

			return new RoomResponseMsg(RoomResponseType.Success, requester, roomName, players);
		}

	}
}