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
			string whoJoined = request.playerName;

			Console.WriteLine($"Handling join game request for: ");
			Console.WriteLine($"\tRoom: {roomName}");
			Console.WriteLine($"\tUser: {whoJoined} ... ");


			var room = pool.GetGame(roomName);
			if (room == null)
			{
				Console.WriteLine($"Tried to join nonexisting room {roomName} ... ");
				sendResponse(whoJoined, roomName, RoomResponseType.InvalidRoom, new List<PlayerData>());
				return MediatR.Unit.Value;
			}

			if (room.HasPlayer(request.playerName))
			{
				Console.WriteLine($"Tried to join room AGAIN {roomName} ... ");
				sendResponse(whoJoined, roomName, RoomResponseType.AlreadyIn, new List<PlayerData>());
				return MediatR.Unit.Value;
			}

			string requiredPassword = pool.GetGame(roomName).GetPassword();
			if (requiredPassword != request.providedPassword)
			{
				Console.WriteLine($"Failed to join room {roomName}, "
					+ "wrongPassword ({request.providedPassword}) ... ");

				sendResponse(whoJoined, roomName, RoomResponseType.WrongPassword, new List<PlayerData>());
				return MediatR.Unit.Value;
			}

			// all good retrieve data and join this dude 

			var dataRequest = new GetPlayerRequest(whoJoined);
			var playerData = await mediator.Send(dataRequest);

			if (playerData != null)
			{
				Console.WriteLine("User data successfully obtained ... ");
				// maybe check level, points ... some requirements for the room ... 
				room.AddPlayer(playerData);

				Console.WriteLine($"{whoJoined} successfully joined to the {roomName} room ... ");

				sendResponse(whoJoined, roomName, RoomResponseType.Success, room.GetPlayers());

				room.GetPlayers()
					.FindAll(p => p.username != whoJoined)
					.ForEach(p => sendUpdate(whoJoined, roomName, room.GetPlayers(), p.username));

				// foreach (var player in room.GetPlayers())
				// {
				// 	if (player.username != request.playerName)
				// 	{
				// 		sendUpdate(whoJoined, roomName, room.GetPlayers(), player.username);
				// 	}

				// }

			}
			else
			{
				Console.WriteLine("Failed to obtain players data ... ");

				sendResponse(whoJoined, roomName, RoomResponseType.UnknownFail, null);
			}


			return MediatR.Unit.Value;
		}

		private void sendResponse(string player, string room, RoomResponseType status,
				List<PlayerData> players)
		{
			var message = new RoomResponseMsg(DateTime.Now, status, player, room, players);
			var sendResponseReq = new SendResponseRequest(room, player, message);

			mediator.Send(sendResponseReq);

			return;
		}

		private void sendUpdate(string whoJoined,
			string room,
			List<PlayerData> players,
			string whomToSend)
		{
			var message = new RoomResponseMsg(DateTime.Now, RoomResponseType.PlayerJoined, whoJoined, room, players);
			var sendReq = new SendUpdateRequest(room, whomToSend, message);

			mediator.Send(sendReq);
		}

	}
}