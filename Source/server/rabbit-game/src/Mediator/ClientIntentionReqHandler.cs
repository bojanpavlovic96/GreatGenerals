using MediatR;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class ClientIntentionReqHandler : IRequestHandler<ClientIntentionRequest, Unit>
	{
		private IMediator mediator;
		private IGamePool pool;

		public ClientIntentionReqHandler(IMediator mediator, IGamePool gamePool)
		{
			this.mediator = mediator;
			this.pool = gamePool;
		}

		public Task<Unit> Handle(ClientIntentionRequest request,
			CancellationToken cancellationToken)
		{

			Console.WriteLine($"Handling client intention: {request.message.type.ToString()} ... ");
			Console.WriteLine($"\tUser: {request.message.username}");
			Console.WriteLine($"\tRoom: {request.message.roomName}");

			var game = pool.GetGame(request.message.roomName);

			var translateReq = new MapMessageToIntentionRequest(request.message);

			// TODO Should this method be async or just force sync with Result 
			// let it be .Result for testing I guess ... 
			var modelEvent = mediator.Send(translateReq).Result;

			Console.WriteLine("Message mapped to client intention ... ");

			if (game == null)
			{
				Console.WriteLine("Requested game does not exists ... ");
			}

			game.AddIntention(modelEvent);


			// if (message == null)
			// {
			// 	Console.WriteLine("Failed to handle model event ... ");
			// 	return Task.FromResult(Unit.Value);
			// }

			// Console.WriteLine($"Request to send {message.type.ToString()} ... ");
			// var sendRequest = new SendMessageRequest(message);
			// mediator.Send(sendRequest);

			return Task.FromResult(Unit.Value);
		}



	}
}