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

			// check if game exists
			// check if there is at least 2 players
			// send response 
			// send update that game is starting 

			// at this point everyone should close room form and request initMessage

			RoomResponseMsg response;


			var game = pool.GetGame(request.roomName);

			if (game == null)
			{
				Console.WriteLine("Starting game for unknown game ... ");
				response = new RoomResponseMsg(RoomResponseType.InvalidRoom,
					request.username,
					request.roomName,
					null);
			}
			else if (!game.isReady())
			{
				Console.WriteLine("Room exists but is not ready ... ");
				response = new RoomResponseMsg(RoomResponseType.GameNotReady,
					request.username,
					request.roomName,
					game.Players);
			}
			else
			{

				game.initGame();

				Console.WriteLine("Game initialized ... ");

				response = RoomResponseMsg.success(request.username,
					request.roomName,
					game.Players);

				foreach (var player in game.Players)
				{
					if (player.username != request.username)
					{
						var update = new RoomResponseMsg(RoomResponseType.GameStarted,
							request.username,
							request.roomName,
							game.Players);


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