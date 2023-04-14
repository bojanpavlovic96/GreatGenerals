using MediatR;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ClientIntentions;

namespace RabbitGameServer.Mediator
{
	public class MapMessageToIntentionReqHandler
		: IRequestHandler<MapMessageToEventRequest, ClientIntention>
	{

		public Task<ClientIntention> Handle(MapMessageToEventRequest request,
			CancellationToken cancellationToken)
		{

			switch (request.message.type)
			{
				case MessageType.ReadyForInitMsg:
					return Task.FromResult<ClientIntention>(new ReadyForInitIntention(
						request.message.username,
						request.message.roomName));

				case MessageType.MoveMessage:
					return Task.FromResult<ClientIntention>(new MoveIntention(
						((MoveMessage)request.message).username,
						((MoveMessage)request.message).startFieldPos,
						((MoveMessage)request.message).endFieldPos));

				case MessageType.AttackMessage:
					return Task.FromResult<ClientIntention>(new AttackIntention(
						request.message.username,
						Enum.Parse<AttackType>(((AttackMessage)request.message).attackType),
						((AttackMessage)request.message).startFieldPos,
						((AttackMessage)request.message).endFieldPos));

				case MessageType.DefendMessage:
					return Task.FromResult<ClientIntention>(new DefendIntention(
						request.message.username,
						Enum.Parse<AttackType>(((DefendMessage)request.message).defendType),
						((DefendMessage)request.message).startFieldPos,
						((DefendMessage)request.message).endFieldPos));

				case MessageType.AbortAttackMessage:
					return Task.FromResult<ClientIntention>(new AbortAttackIntention(
						request.message.username,
						((AbortAttackMessage)request.message).unitPosition));

				case MessageType.BuildUnit:
					return Task.FromResult<ClientIntention>(new BuildUnitIntention(
						request.message.username,
						((BuildUnitMessage)request.message).field,
						((BuildUnitMessage)request.message).unitType));

				default:
					Console.WriteLine($"Failed to map message-{request.message.type.ToString()} to modelEvent ... ");
					return Task.FromResult<ClientIntention>(null); ;
			};

		}
	}
}