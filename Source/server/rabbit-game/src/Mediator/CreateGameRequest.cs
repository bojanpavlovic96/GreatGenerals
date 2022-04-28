using MediatR;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class CreateGameRequest : IRequest<GameMaster>
	{
		public string roomName { get; set; }
		public string masterPlayer { get; set; }

		public CreateGameRequest(string roomName, string masterPlayer)
		{
			this.roomName = roomName;
			this.masterPlayer = masterPlayer;
		}
	}
}