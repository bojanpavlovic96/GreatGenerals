
using MediatR;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class LeaveRoomReqHandler : IRequestHandler<LeaveRoomRequest, MediatR.Unit>
	{
		private IMediator mediator;
		private IGamePool gamePool;

		public LeaveRoomReqHandler(IMediator mediator, IGamePool gamePool)
		{
			this.mediator = mediator;
			this.gamePool = gamePool;
		}

		public Task<MediatR.Unit> Handle(LeaveRoomRequest request, CancellationToken cToken)
		{

			var game = gamePool.GetGame(request.roomName);

			RoomResponseMsg responseMsg;

			if (game == null)
			{
				Console.WriteLine($"Requested to leave nonexistent room: {request.roomName}");
				responseMsg = new RoomResponseMsg(RoomResponseType.InvalidRoom,
						request.username,
						request.roomName,
						null);

			}
			else if (!game.hasPlayer(request.username))
			{
				Console.WriteLine($"There is not such player: " + request.username);
				responseMsg = new RoomResponseMsg(RoomResponseType.InvalidPlayer,
						request.username,
						request.roomName,
						null);
			}
			else
			{
				// Player that requested to leave will receive roomResponse.

				// If player that requested to leave was room master:
				// others will receive roomDestroyed update.
				// else
				// others will receive playerLeft update.

				Console.WriteLine("Player successfully left the room ... ");

				responseMsg = RoomResponseMsg.success(request.username,
						request.roomName,
						game.Players);
				// Since this player left the room he does't really need to know who
				// else is till in the room but I will leave game.Players instead of null
				// or empty list as the last argument just for debug purposes. 

				game.removePlayer(request.username);


				RoomResponseType updateType;

				Console.WriteLine("Forming response ... ");
				if (game.isMaster(request.username) || game.Players.Count == 0)
				{
					updateType = RoomResponseType.RoomDestroyed;
				}
				else
				{
					updateType = RoomResponseType.PlayerLeft;
				}

				Console.WriteLine("Sending updates ... ");
				foreach (var player in game.Players)
				{
					var updateMsg = new RoomResponseMsg(updateType,
							request.username,
							request.roomName,
							game.Players);

					var updateReq = new SendUpdateRequest(request.roomName,
							player.username,
							updateMsg);

					mediator.Send(updateReq);
				}

				if (game.isMaster(request.username) || game.Players.Count == 0)
				{
					gamePool.destroyGame(request.roomName);
					Console.WriteLine("Room will be destroyed ... ");
				}

			}

			Console.WriteLine("Sending response ... ");
			var sendReq = new SendResponseRequest(request.roomName,
					request.username,
					responseMsg);

			mediator.Send(sendReq);


			return Task.FromResult(MediatR.Unit.Value);
		}

	}
}