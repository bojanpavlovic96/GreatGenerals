using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class CreateGameReqHandler : IRequestHandler<CreateGameRequest, GameMaster>
	{

		private IMediator mediator;

		private RabbitConfig config;

		private GamePool pool;

		public CreateGameReqHandler(IMediator mediator, IOptions<RabbitConfig> config, GamePool pool)
		{
			this.mediator = mediator;

			this.config = config.Value;
			this.pool = pool;
		}

		public Task<GameMaster> Handle(CreateGameRequest request,
			CancellationToken cancellationToken)
		{
			var game = pool.CreateGame(request.roomName, request.players);

			// TODO publish command to other players

			return Task.FromResult(game);
		}
	}
}