using MediatR;
using RabbitGameServer.SharedModel.Commands;

namespace RabbitGameServer.Mediator
{
	public class CommandSendRequest : IRequest<Unit>
	{
		public Command command { get; set; }
	}
}