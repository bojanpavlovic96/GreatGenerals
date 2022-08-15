
using MediatR;
using RabbitGameServer.Client;

namespace RabbitGameServer.Mediator
{
	public class SendUpdateReqHandler : IRequestHandler<SendUpdateRequest, Unit>
	{

		private IPlayerProxy proxy;

		public SendUpdateReqHandler(IPlayerProxy proxy)
		{
			this.proxy = proxy;
		}

		public Task<Unit> Handle(SendUpdateRequest request, CancellationToken cToken)
		{

			Console.WriteLine("Handling sendUpdate request ... ");
			proxy.sendUpdate(request.RoomName, request.PlayerName, request.Message);

			return Task.FromResult(Unit.Value);
		}
	}
}