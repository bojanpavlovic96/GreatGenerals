using MediatR;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class JoinRoomReqHandler : IRequestHandler<JoinRoomRequest, MediatR.Unit>
	{

		private IMediator mediator;
		private IGamePool pool;

		public JoinRoomReqHandler(IMediator mediator, IGamePool gamePool)
		{
			this.mediator = mediator;
			this.pool = gamePool;
		}

		public Task<MediatR.Unit> Handle(JoinRoomRequest request, CancellationToken cToken)
		{

			string roomName = request.roomName;
			string playerName = request.playerName;

			var room = pool.GetGame(roomName);
			if (room == null)
			{
				Console.WriteLine($"Tried to join nonexisting room {roomName} ... ");
				sendResponse(playerName, roomName, RoomResponseType.InvalidRoom, new List<PlayerData>());
				return Task.FromResult(MediatR.Unit.Value);
			}

			if (room.hasPlayer(request.playerName))
			{
				Console.WriteLine($"Tried to join room AGAIN {roomName} ... ");
				sendResponse(playerName, roomName, RoomResponseType.AlreadyIn, new List<PlayerData>());
				return Task.FromResult(MediatR.Unit.Value);
			}

			string requiredPassword = pool.GetGame(roomName).Password;
			if (requiredPassword != request.providedPassword)
			{
				Console.WriteLine($"Failed to join room {roomName}, "
					+ "wrongPassword ({request.providedPassword}) ... ");

				sendResponse(playerName, roomName, RoomResponseType.WrongPassword, new List<PlayerData>());
				return Task.FromResult(MediatR.Unit.Value);
			}

			// all good join this dude 

			room.addPlayer(request.playerName);

			var players = new List<PlayerData>();
			foreach (var sPlayer in room.Players)
			{
				players.Add(new PlayerData(sPlayer, null));
			}

			Console.WriteLine($"{request.playerName} successfully joined {roomName} ... ");
			sendResponse(playerName, roomName, RoomResponseType.Success, players);

			return Task.FromResult(MediatR.Unit.Value);
		}

		private void sendResponse(string player,
				string room,
				RoomResponseType status,
				List<PlayerData> players)
		{
			var message = new RoomResponseMsg(player, room, status, players);
			var sendResponseReq = new SendResponseRequest(room, player, message);

			var sendUpdateReq = new SendUpdateRequest(room, player, message);

			mediator.Send(sendResponseReq);
			mediator.Send(sendUpdateReq);

			return;
		}

	}
}