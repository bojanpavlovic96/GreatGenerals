
namespace RabbitGameServer.SharedModel.Messages
{
	public class LeaveRoomMessage : Message
	{
		public LeaveRoomMessage(DateTime timestamp, string player, string roomName)
			: base(MessageType.LeaveRoomRequest,timestamp, player, roomName)
		{
            
		}
	}
}