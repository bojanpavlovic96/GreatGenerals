using MediatR;
using RabbitGameServer.Client;

namespace RabbitGameServer.Mediator
{
	public class CommandSendReqHandler : IRequestHandler<CommandSendRequest, Unit>
	{

		private IPlayerProxy playerProxy;

		public CommandSendReqHandler(IPlayerProxy proxy)
		{
			this.playerProxy = proxy;
		}

		public Task<Unit> Handle(CommandSendRequest request, CancellationToken cancellationToken)
		{

			playerProxy.sendCommand(request.RoomName, request.PlayerId, request.Command);

			return Task.FromResult(Unit.Value);
		}
	}
}