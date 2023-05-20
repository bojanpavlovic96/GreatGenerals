using MediatR;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class CreateRoomReqHandler : IRequestHandler<CreateRoomRequest, MediatR.Unit>
	{
		private IMediator mediator;

		private IGamePool pool;

		public CreateRoomReqHandler(IMediator mediator, IGamePool pool)
		{
			this.mediator = mediator;

			this.pool = pool;
		}

		public async Task<MediatR.Unit> Handle(CreateRoomRequest request, CancellationToken cancelToken)
		{
			Console.WriteLine($"Handling create game request for: ");
			Console.WriteLine($"\tRoom: {request.roomName}");
			Console.WriteLine($"\tUser: {request.masterPlayer} ... ");

			Message message = null;

			if (pool.GetGame(request.roomName) != null)
			{
				Console.WriteLine("Requested room already exists ... ");

				message = new RoomResponseMsg(DateTime.Now,
					RoomResponseType.InvalidRoom,
					request.masterPlayer,
					request.roomName,
					new List<PlayerData>());
			}
			else
			{
				var dataRequest = new GetPlayerRequest(request.masterPlayer);
				var playerData = await mediator.Send(dataRequest);

				if (playerData != null)
				{

					Console.WriteLine("User data successfully obtained ... ");

					var newGame = pool.CreateGame(request.roomName,
						request.password,
						playerData);

					message = new RoomResponseMsg(DateTime.Now,
						RoomResponseType.Success,
						request.masterPlayer,
						request.roomName,
						newGame.GetPlayers());
				}
				else
				{
					Console.WriteLine("Failed to obtain player data ... ");

					message = RoomResponseMsg.unknownFail();
				}

			}

			var publishRequest = new SendResponseRequest(request.roomName,
					request.masterPlayer,
					message);

			mediator.Send(publishRequest);

			// return Task.FromResult(MediatR.Unit.Value);
			return MediatR.Unit.Value;
		}
	}
}