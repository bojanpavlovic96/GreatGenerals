using MediatR;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class ModelEventRequest : IRequest<Unit>
	{

		public Message message;

		public ModelEventRequest(Message message)
		{
			this.message = message;
		}

	}
}