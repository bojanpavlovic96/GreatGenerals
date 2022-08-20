
namespace RabbitGameServer.SharedModel.Messages
{
	public class RoomResponseMsg : Message
	{
		public RoomResponseType responseType;

		public List<PlayerData> players;

		public RoomResponseMsg(string player,
			string roomName,
			RoomResponseType type,
			List<PlayerData> players)
			: base(MessageType.JoinResponse, player, roomName)
		{
			this.responseType = type;
			this.players = players;
		}
	}
}