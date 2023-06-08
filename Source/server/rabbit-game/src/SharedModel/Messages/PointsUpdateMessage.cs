
namespace RabbitGameServer.SharedModel.Messages
{
	public class PointsUpdateMessage : Message
	{
		public int income { get; set; }
		public int totalAmount { get; set; }

		public PointsUpdateMessage(DateTime timestamp, string username, string roomName, int income, int totalAmount)
			: base(MessageType.PointsUpdate, timestamp, username, roomName)
		{
			this.income = income;
			this.totalAmount = totalAmount;
		}
	}
}