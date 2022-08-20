
using MediatR;

namespace RabbitGameServer.Mediator
{
	public class LeaveRoomRequest : IRequest<Unit>
	{

		public String roomName { get; set; }
		public String username { get; set; }

		public LeaveRoomRequest(string roomName, string username)
		{
			this.roomName = roomName;
			this.username = username;
		}
	}
}