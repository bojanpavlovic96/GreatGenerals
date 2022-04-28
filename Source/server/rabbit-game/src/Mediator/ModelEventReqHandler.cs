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

			var game = pool.GetGame(request.message.roomName);

			// TODO at this point message should be converted to modelEvent ... ? 
			// game.AddModelEvent(request.modelEvent);

			return Task.FromResult(Unit.Value);
		}



	}
}