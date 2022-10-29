using MediatR;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Mediator
{
	public class MapMessageToEventReqHandler
		: IRequestHandler<MapMessageToEventRequest, ModelEvent>
	{

		public Task<ModelEvent> Handle(MapMessageToEventRequest request,
			CancellationToken cancellationToken)
		{

			switch (request.message.type)
			{
				case MessageType.MoveMessage:
					return Task.FromResult<ModelEvent>(new MoveModelEvent(
						((MoveMessage)request.message).username,
						((MoveMessage)request.message).startFieldPos,
						((MoveMessage)request.message).endFieldPos));

				case MessageType.ReadyForInitMsg:
					return Task.FromResult<ModelEvent>(new ReadyForInitModelEvent(
						request.message.username,
						request.message.roomName));

				case MessageType.AttackMessage:
					return Task.FromResult<ModelEvent>(new AttackModelEvent(
						request.message.username,
						((AttackMessage)request.message).startFieldPos,
						((AttackMessage)request.message).endFieldPos));


			};

			return Task.FromResult<ModelEvent>(null);
		}
	}
}