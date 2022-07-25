using MediatR;

namespace RabbitGameServer.Mediator
{
	public class JoinGameRequest : IRequest<Unit>
	{

		public string roomName { get; set; }
		public string playerName { get; set; }
		public string providedPassword { get; set; }

		public JoinGameRequest(string roomName, string playerName, string passwd)
		{
			this.roomName = roomName;
			this.playerName = playerName;
			this.providedPassword = passwd;
		}

	}
}