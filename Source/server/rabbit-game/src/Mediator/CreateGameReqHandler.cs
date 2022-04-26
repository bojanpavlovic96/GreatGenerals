using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel.Commands;

namespace RabbitGameServer.Mediator
{
	public class CreateGameReqHandler : IRequestHandler<CreateGameRequest, GameMaster>
	{

		private IMediator mediator;

		private RabbitConfig config;

		private IGamePool pool;

		public CreateGameReqHandler(IMediator mediator,
			IOptions<RabbitConfig> config,
			IGamePool pool)
		{
			this.mediator = mediator;

			this.config = config.Value;
			this.pool = pool;
		}

		public Task<GameMaster> Handle(CreateGameRequest request,
			CancellationToken cancellationToken)
		{
			Console.WriteLine("Handling create game request for:  " + request.roomName);
			var game = pool.CreateGame(request.roomName, request.players);

			var command = new InitializeGameCommand();

			var publishRequest = new CommandSendRequest(game.RoomName, null, command);
			mediator.Send(publishRequest);

			return Task.FromResult(game);
		}
	}
}