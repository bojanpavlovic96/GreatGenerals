
using MediatR;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{

	public class SendUpdateRequest : IRequest<Unit>
	{

		public String RoomName { get; set; }
		public String PlayerName { get; set; }

		public Message Message { get; set; }

		public SendUpdateRequest(string roomName, string playerName, Message message)
		{
			RoomName = roomName;
			PlayerName = playerName;
			Message = message;
		}
	}
}