
using MediatR;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ClientIntentions;

namespace RabbitGameServer.Mediator
{
	public class MapMessageToIntentionRequest : IRequest<ClientIntention>
	{
		public Message message;

		public MapMessageToIntentionRequest(Message message)
		{
			this.message = message;
		}
	}
}