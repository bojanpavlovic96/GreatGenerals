namespace RabbitGameServer.SharedModel.Messages
{
	public class IncomeTickMessage : Message
	{
		public int amount { get; set; }

		public IncomeTickMessage(DateTime timestamp, string username, string roomName, int amount)
			: base(MessageType.IncomeTick, timestamp, username, roomName)
		{
			this.amount = amount;
		}
	}
}