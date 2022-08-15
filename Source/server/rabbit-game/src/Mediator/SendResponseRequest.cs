using MediatR;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class SendResponseRequest : IRequest<Unit>
	{
		public string RoomName { get; set; }
		public string PlayerName { get; set; }

		public Message Message { get; set; }

		public SendResponseRequest(string roomName, string playerName, Message message)
		{
			RoomName = roomName;
			PlayerName = playerName;
			Message = message;
		}
	}
}