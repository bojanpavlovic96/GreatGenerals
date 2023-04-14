
using MediatR;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ClientIntentions;

namespace RabbitGameServer.Mediator
{
	public class MapMessageToEventRequest : IRequest<ClientIntention>
	{
		public Message message;

		public MapMessageToEventRequest(Message message)
		{
			this.message = message;
		}
	}
}