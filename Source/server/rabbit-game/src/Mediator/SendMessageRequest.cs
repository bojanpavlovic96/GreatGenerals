using MediatR;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class SendMessageRequest : IRequest<Unit>
	{
		public string RoomName { get; set; }
		public string PlayerName { get; set; }

		public Message Message { get; set; }

		public SendMessageRequest(string roomName, string playerName, Message message)
		{
			RoomName = roomName;
			PlayerName = playerName;
			Message = message;
		}
	}
}