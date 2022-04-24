using MediatR;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class CreateGameRequest : IRequest<GameMaster>
	{
		public string roomName { get; set; }

		public List<string> players { get; set; }
	}
}