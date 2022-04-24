using MediatR;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Mediator
{
	public class ModelEventRequest : IRequest<Unit>
	{

		public ModelEvent modelEvent { get; set; }

		public ModelEventRequest(ModelEvent modelEvent)
		{
			this.modelEvent = modelEvent;
		}

	}
}