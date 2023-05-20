namespace RabbitGameServer.SharedModel.Messages
{
	public class AbortAttackMessage : Message
	{
		public Point2D unitPosition;

		public AbortAttackMessage(DateTime timestamp, string username, string roomName, Point2D unitPosition)
			: base(MessageType.AbortAttackMessage, timestamp, username, roomName)
		{
			this.unitPosition = unitPosition;
		}
	}
}