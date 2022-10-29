using MediatR;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class SendMessageRequest : IRequest<Unit>
	{

		public Message message { get; set; }

		public SendMessageRequest(Message message)
		{
			this.message = message;
		}
	}
}