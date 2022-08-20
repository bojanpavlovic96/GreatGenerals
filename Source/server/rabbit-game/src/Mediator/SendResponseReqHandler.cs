using MediatR;
using RabbitGameServer.Client;

namespace RabbitGameServer.Mediator
{
	public class SendResponseReqHandler : IRequestHandler<SendResponseRequest, Unit>
	{

		private IPlayerProxy playerProxy;

		public SendResponseReqHandler(IPlayerProxy proxy)
		{
			this.playerProxy = proxy;
		}

		public Task<Unit> Handle(SendResponseRequest request, CancellationToken cToken)
		{
			Console.WriteLine("Handling sendResponse request ... ");
			playerProxy.sendResponse(request.RoomName, request.PlayerName, request.Message);

			return Task.FromResult(Unit.Value);
		}
	}
}