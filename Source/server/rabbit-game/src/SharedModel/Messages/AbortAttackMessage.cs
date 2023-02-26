namespace RabbitGameServer.SharedModel.Messages
{
	public class AbortAttackMessage : Message
	{
		public Point2D unitPosition;

		public AbortAttackMessage(string username, string roomName, Point2D unitPosition)
			: base(MessageType.AbortAttackMessage, username, roomName)
		{
			this.unitPosition = unitPosition;
		}
	}
}