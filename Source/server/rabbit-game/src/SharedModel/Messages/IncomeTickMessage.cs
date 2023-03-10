namespace RabbitGameServer.SharedModel.Messages
{
	public class IncomeTickMessage : Message
	{
		public int amount { get; set; }

		public IncomeTickMessage(string username, string roomName, int amount)
			: base(MessageType.IncomeTick, username, roomName)
		{
			this.amount = amount;
		}
	}
}