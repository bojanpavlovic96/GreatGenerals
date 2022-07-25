using MediatR;

namespace RabbitGameServer.Mediator
{
	public class CreateRoomRequest : IRequest<Unit>
	{
		public string roomName { get; set; }
		public string masterPlayer { get; set; }

		public string password { get; set; }

		public CreateRoomRequest(string roomName, string passwd, string masterPlayer)
		{
			this.roomName = roomName;
			this.masterPlayer = masterPlayer;
			this.password = passwd;
		}
	}
}