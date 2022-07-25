using MediatR;
using RabbitGameServer.Client;

namespace RabbitGameServer.Mediator
{
	public class SendMessageReqHandler : IRequestHandler<SendMessageRequest, Unit>
	{

		private IPlayerProxy playerProxy;

		public SendMessageReqHandler(IPlayerProxy proxy)
		{
			this.playerProxy = proxy;
		}

		public Task<Unit> Handle(SendMessageRequest request, CancellationToken cancellationToken)
		{
			Console.WriteLine("Handling sendMessage request ... ");
			playerProxy.sendMessage(request.RoomName, request.PlayerName, request.Message);

			return Task.FromResult(Unit.Value);
		}
	}
}