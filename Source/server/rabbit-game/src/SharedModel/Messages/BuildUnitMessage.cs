
namespace RabbitGameServer.SharedModel.Messages
{
	public class BuildUnitMessage : Message
	{

		public Point2D field { get; set; }
		public string unitType { get; set; }
		public int cost { get; set; }

		public BuildUnitMessage(DateTime timestamp, 
			string username,
			string roomName,
			Point2D field,
			String unitType,
			int cost)
			: base(MessageType.BuildUnit,timestamp, username, roomName)
		{
			this.field = field;
			this.unitType = unitType;
			this.cost = cost;
		}
	}
}