using MediatR;

namespace rabbit_game.src.Mediator
{
	public class PlayReplayRequest : IRequest<Unit>
	{
		public string roomId;
		public string requester;
	}
}