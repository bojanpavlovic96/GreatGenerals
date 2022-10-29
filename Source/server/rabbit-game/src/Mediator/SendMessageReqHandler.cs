using MediatR;
using RabbitGameServer.Client;

namespace RabbitGameServer.Mediator
{
	public class SendMessageReqHandler : IRequestHandler<SendMessageRequest, Unit>
	{
		private IPlayerProxy proxy;

		public SendMessageReqHandler(IPlayerProxy proxy)
		{
			this.proxy = proxy;
		}

		public Task<Unit> Handle(SendMessageRequest request, CancellationToken cToken)
		{

			Console.WriteLine($"Sending server message: {request.message.type.ToString()} ... ");

			proxy.sendMessage(request.message.roomName,
				request.message.username,
				request.message);

			return Task.FromResult<Unit>(Unit.Value);
		}
	}
}