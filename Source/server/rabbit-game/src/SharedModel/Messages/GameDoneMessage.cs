using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.SharedModel.Messages
{
	public class GameDoneMessage : Message
	{
		public int bonusAmount { get; set; }

		public GameDoneMessage(string username,
			string roomName,
			int bonusAmount)
			: base(MessageType.GameDone, username, roomName)
		{
			this.bonusAmount = bonusAmount;
		}
	}
}