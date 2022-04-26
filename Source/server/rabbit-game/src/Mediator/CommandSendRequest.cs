using MediatR;
using RabbitGameServer.SharedModel.Commands;

namespace RabbitGameServer.Mediator
{
	public class CommandSendRequest : IRequest<Unit>
	{
		public string RoomName { get; set; }
		public string PlayerId { get; set; }

		public Command Command { get; set; }

		public CommandSendRequest(string roomName, string playerId, Command command)
		{
			RoomName = roomName;
			PlayerId = playerId;
			Command = command;
		}
	}
}