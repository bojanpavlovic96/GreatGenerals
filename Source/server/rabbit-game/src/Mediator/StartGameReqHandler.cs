using MediatR;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class StartGameReqHandler : IRequestHandler<StartGameRequest, Unit>
	{

		private IGamePool pool;

		public StartGameReqHandler(IGamePool pool)
		{
			this.pool = pool;
		}

		public Task<Unit> Handle(StartGameRequest request, CancellationToken cancellationToken)
		{
			Console.WriteLine($"Handling startGameRequest for room: {request.roomName}");

			var game = pool.GetGame(request.roomName);

			

			return Task.FromResult(Unit.Value);
		}
	}
}