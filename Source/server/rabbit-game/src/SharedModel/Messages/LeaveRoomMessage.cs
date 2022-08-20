
namespace RabbitGameServer.SharedModel.Messages
{
	public class LeaveRoomMessage : Message
	{
		public LeaveRoomMessage(string player, string roomName)
			: base(MessageType.LeaveRoomRequest, player, roomName)
		{
            
		}
	}
}