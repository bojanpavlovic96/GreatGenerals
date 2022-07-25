using MediatR;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class JoinGameReqHandler : IRequestHandler<JoinGameRequest, Unit>
	{

		private IMediator mediator;
		private IGamePool pool;

		public JoinGameReqHandler(IMediator mediator, IGamePool gamePool)
		{
			this.mediator = mediator;
			this.pool = gamePool;
		}

		public Task<Unit> Handle(JoinGameRequest request,
			CancellationToken cancellationToken)
		{

			string roomName = request.roomName;
			var room = pool.GetGame(roomName);
			if (room == null)
			{
				Console.WriteLine($"Tried to join nonexisting room {roomName} ... ");
				sendStatusCommand(request.playerName, JoinResponseType.InvalidRoom);
				return Task.FromResult(Unit.Value);
			}

			if (room.hasPlayer(request.playerName))
			{
				Console.WriteLine($"Tried to join room AGAIN {roomName} ... ");
				sendStatusCommand(request.playerName, JoinResponseType.AlreadyIn);
				return Task.FromResult(Unit.Value);
			}

			string requiredPassword = pool.GetGame(roomName).Password;
			if (requiredPassword != request.providedPassword)
			{
				Console.WriteLine($"Failed to join room {roomName}, "
					+ "wrongPassword ({request.providedPassword}) ... ");
				sendStatusCommand(request.playerName, JoinResponseType.WrongPassword);
				return Task.FromResult(Unit.Value);
			}

			// all good join this dude 

			room.addPlayer(request.playerName);

			Console.WriteLine($"{request.playerName} successfully joined {roomName} ... ");
			sendStatusCommand(request.playerName, JoinResponseType.Success);

			return Task.FromResult(Unit.Value);
		}

		private void sendStatusCommand(string player, JoinResponseType status)
		{
			var command = new JoinResponseMsg(player, "", status);
			var message = new SendMessageRequest("", player, command);

			mediator.Send(message);

			return;
		}

	}
}