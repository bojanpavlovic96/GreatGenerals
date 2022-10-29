
namespace RabbitGameServer.SharedModel.Messages
{
	public class AbortMoveMessage : Message
	{
		public Point2D unitPosition;

		public AbortMoveMessage(string username, string roomName, Point2D unitPos)
			: base(MessageType.AbortMoveMessage, username, roomName)
		{
			this.unitPosition = unitPos;
		}
	}
}