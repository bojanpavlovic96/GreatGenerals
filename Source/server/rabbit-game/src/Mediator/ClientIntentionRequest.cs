using MediatR;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class ClientIntentionRequest : IRequest<Unit>
	{

		public Message message;

		public ClientIntentionRequest(Message message)
		{
			this.message = message;
		}

	}
}