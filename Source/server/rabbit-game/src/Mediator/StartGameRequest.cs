using MediatR;

namespace RabbitGameServer.Mediator
{
	public class StartGameRequest : IRequest<Unit>
	{
		public String roomName { get; set; }
		public String username { get; set; }

		public StartGameRequest(string roomName, string username)
		{
			this.roomName = roomName;
			this.username = username;
		}
	}
}