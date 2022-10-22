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

		public async Task<MediatR.Unit> Handle(JoinRoomRequest request, CancellationToken cToken)
		{

			string roomName = request.roomName;
			string playerName = request.playerName;

			Console.WriteLine($"Handling join game request for: ");
			Console.WriteLine($"\tRoom: {roomName}");
			Console.WriteLine($"\tUser: {playerName} ... ");


			var room = pool.GetGame(roomName);
			if (room == null)
			{
				Console.WriteLine($"Tried to join nonexisting room {roomName} ... ");
				sendResponse(playerName, roomName, RoomResponseType.InvalidRoom, new List<PlayerData>());
				return MediatR.Unit.Value;
			}

			if (room.hasPlayer(request.playerName))
			{
				Console.WriteLine($"Tried to join room AGAIN {roomName} ... ");
				sendResponse(playerName, roomName, RoomResponseType.AlreadyIn, new List<PlayerData>());
				return MediatR.Unit.Value;
			}

			string requiredPassword = pool.GetGame(roomName).Password;
			if (requiredPassword != request.providedPassword)
			{
				Console.WriteLine($"Failed to join room {roomName}, "
					+ "wrongPassword ({request.providedPassword}) ... ");

				sendResponse(playerName, roomName, RoomResponseType.WrongPassword, new List<PlayerData>());
				return MediatR.Unit.Value;
			}

			// all good retrieve data and join this dude 

			var dataRequest = new GetPlayerRequest(playerName);
			var playerData = await mediator.Send(dataRequest);

			if (playerData != null)
			{
				Console.WriteLine("User data successfully obtained ... ");
				// maybe check level, points ... some requirements form the room ... 
				room.addPlayer(playerData);

				Console.WriteLine($"{request.playerName} successfully joined {roomName} ... ");

				sendResponse(playerName, roomName, RoomResponseType.Success, room.Players);
				sendUpdate(playerName, roomName, RoomResponseType.PlayerJoined, room.Players);

			}
			else
			{
				Console.WriteLine("Failed to obtain players data ... ");

				sendResponse(playerName, roomName, RoomResponseType.UnknownFail, null);
			}


			return MediatR.Unit.Value;
		}

		private void sendResponse(string player, string room, RoomResponseType status,
				List<PlayerData> players)
		{
			var message = new RoomResponseMsg(status, player, room, players);
			var sendResponseReq = new SendResponseRequest(room, player, message);

			mediator.Send(sendResponseReq);

			return;
		}

		private void sendUpdate(string player,
			string room,
			RoomResponseType type,
			List<PlayerData> players)
		{
			var message = new RoomResponseMsg(type, player, room, players);
			var sendReq = new SendUpdateRequest(room, player, message);

			mediator.Send(sendReq);
		}

	}
}