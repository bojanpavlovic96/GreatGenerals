using MediatR;
using RabbitGameServer.Game;

namespace RabbitGameServer.Mediator
{
	public class ModelEventReqHandler : IRequestHandler<ModelEventRequest, Unit>
	{
		private IMediator mediator;
		private IGamePool pool;

		public ModelEventReqHandler(IMediator mediator, IGamePool gamePool)
		{
			this.mediator = mediator;
			this.pool = gamePool;
		}

		public Task<Unit> Handle(ModelEventRequest request,
			CancellationToken cancellationToken)
		{

			Console.WriteLine($"Handling model event: {request.message.type.ToString()} ... ");
			Console.WriteLine($"\tUser: {request.message.username}");
			Console.WriteLine($"\tRoom: {request.message.roomName}");

			var game = pool.GetGame(request.message.roomName);

			var translateReq = new MapMessageToEventRequest(request.message);

			// TODO Should this method be async or just force sync with Result 
			// let it be .Result for testing I guess ... 
			var modelEvent = mediator.Send(translateReq).Result;

			var message = game.AddModelEvent(modelEvent);

			if (message == null)
			{
				Console.WriteLine("Failed to handle model event ... ");
				return Task.FromResult(Unit.Value);
			}

			var sendRequest = new SendMessageRequest(message);
			mediator.Send(sendRequest);

			return Task.FromResult(Unit.Value);
		}



	}
}