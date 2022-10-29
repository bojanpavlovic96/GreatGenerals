
using MediatR;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Mediator
{
	public class MapMessageToEventRequest : IRequest<ModelEvent>
	{
		public Message message;

		public MapMessageToEventRequest(Message message)
		{
			this.message = message;
		}
	}
}