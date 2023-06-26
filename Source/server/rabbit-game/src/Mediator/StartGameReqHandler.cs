using MediatR;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class StartGameReqHandler : IRequestHandler<StartGameRequest, Unit>
	{

		private IGamePool pool;
		private IMediator mediator;

		public StartGameReqHandler(IGamePool pool, IMediator mediator)
		{
			this.pool = pool;
			this.mediator = mediator;
		}

		public Task<Unit> Handle(StartGameRequest request, CancellationToken cancellationToken)
		{
			Console.WriteLine($"Handling startGameRequest for room: {request.roomName}");

			RoomResponseMsg response;

			var game = pool.GetGame(request.roomName);

			if (game == null)
			{
				Console.WriteLine("Starting game for unknown game ... ");
				response = new RoomResponseMsg(DateTime.Now, RoomResponseType.InvalidRoom,
					request.username,
					request.roomName,
					null);
			}
			else if (!game.IsReady())
			{
				Console.WriteLine("Room exists but is not ready ... ");
				response = new RoomResponseMsg(DateTime.Now,
					RoomResponseType.GameNotReady,
					request.username,
					request.roomName,
					game.GetPlayers());
			}
			else
			{

				game.InitGame();

				Console.WriteLine("Game initialized ... ");

				response = RoomResponseMsg.success(request.username,
					request.roomName,
					game.GetPlayers());

				foreach (var player in game.GetPlayers())
				{
					if (player.username != request.username)
					{
						var update = new RoomResponseMsg(DateTime.Now,
							RoomResponseType.GameStarted,
							request.username,
							request.roomName,
							game.GetPlayers());


						var sendUpdateReq = new SendUpdateRequest(request.roomName,
							player.username,
							update);

						mediator.Send(sendUpdateReq);
					}
					else
					{
						Console.WriteLine($"\tAvoiding {player.username} ... ");
					}
				}

			}

			var sendResponseReq = new SendResponseRequest(request.roomName,
					request.username,
					response);

			mediator.Send(sendResponseReq);

			return Task.FromResult(Unit.Value);
		}
	}
}