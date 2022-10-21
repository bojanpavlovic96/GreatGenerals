
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

			var responseMsg = new RoomResponseMsg(request.username, request.roomName,
					RoomResponseType.UnknownFail, new List<PlayerData>());

			if (game == null)
			{
				Console.WriteLine($"Requested to leave nonexistent room: {request.roomName}");

				responseMsg.responseType = RoomResponseType.InvalidRoom;
			}
			else
			{

				if (!game.hasPlayer(request.username))
				{
					Console.WriteLine($"There is not such player: " + request.username);
					responseMsg.responseType = RoomResponseType.InvalidPlayer;
				}
				else
				{
					// Player that requested to leave will receive roomResponse and
					// other players will receive roomUpdate.
					// If player that requested to leave was room master other players
					// will receive roomDestroyed update and
					// if not others will receive playerLeft update.

					Console.WriteLine("Player successfully left the room ... ");
					responseMsg.responseType = RoomResponseType.Success;

					game.removePlayer(request.username);

					var responseType = RoomResponseType.PlayerLeft;
					var playersData = game.Players;

					if (game.isMaster(request.username))
					{
						responseType = RoomResponseType.RoomDestroyed;
						gamePool.destroyGame(request.roomName);
					}

					foreach (var player in playersData)
					{
						var message = new RoomResponseMsg(player.username,
								request.roomName,
								responseType,
								playersData);

						var updateReq = new SendUpdateRequest(request.roomName,
								player.username,
								message);

						mediator.Send(updateReq);
					}

				}

			}

			var sendReq = new SendResponseRequest(request.roomName,
					request.username,
					responseMsg);

			mediator.Send(sendReq);


			return Task.FromResult(MediatR.Unit.Value);
		}

	}
}