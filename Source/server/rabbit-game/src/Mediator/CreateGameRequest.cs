using MediatR;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class CreateGameRequest : IRequest<GameMaster>
	{
		public string roomName { get; set; }

		public List<string> players { get; set; }

		public CreateGameRequest(string roomName, List<string> players)
		{
			this.roomName = roomName;
			this.players = players;
		}
	}
}