using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class CreateRoomReqHandler : IRequestHandler<CreateRoomRequest, Unit>
	{
		private IMediator mediator;

		private RabbitConfig config;

		private IGamePool pool;

		public CreateRoomReqHandler(IMediator mediator,
			IOptions<RabbitConfig> config,
			IGamePool pool)
		{
			this.mediator = mediator;

			this.config = config.Value;
			this.pool = pool;
		}

		public Task<Unit> Handle(CreateRoomRequest request, CancellationToken cancelToken)
		{
			Console.WriteLine("Handling create game request for:  " + request.roomName);

			Message message;

			if (pool.GetGame(request.roomName) != null)
			{
				Console.WriteLine("Requested room already exists ... ");

				message = new JoinResponseMsg(request.masterPlayer,
					request.roomName,
					JoinResponseType.InvalidRoom);
			}
			else
			{
				Console.WriteLine("Requested room created ... ");

				pool.CreateGame(request.roomName,
					request.masterPlayer,
					request.password);

				message = new JoinResponseMsg(request.masterPlayer,
					request.roomName,
					JoinResponseType.Success);
			}

			var publishRequest = new SendMessageRequest(request.roomName,
					request.masterPlayer,
					message);

			mediator.Send(publishRequest);

			return Task.FromResult(Unit.Value);
		}
	}
}