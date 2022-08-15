using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel;
// using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Mediator
{
	public class CreateRoomReqHandler : IRequestHandler<CreateRoomRequest, MediatR.Unit>
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

		public Task<MediatR.Unit> Handle(CreateRoomRequest request, CancellationToken cancelToken)
		{
			Console.WriteLine("Handling create game request for:  " + request.roomName);

			Message message;

			if (pool.GetGame(request.roomName) != null)
			{
				Console.WriteLine("Requested room already exists ... ");

				message = new RoomResponseMsg(request.masterPlayer,
					request.roomName,
					RoomResponseType.InvalidRoom,
					new List<PlayerData>());
			}
			else
			{
				Console.WriteLine("Requested room created ... ");

				pool.CreateGame(request.roomName,
					request.masterPlayer,
					request.password);

				var players = new List<PlayerData>();
				players.Add(new PlayerData(request.masterPlayer, Color.RED));

				message = new RoomResponseMsg(request.masterPlayer,
					request.roomName,
					RoomResponseType.Success,
					players);
			}

			var publishRequest = new SendResponseRequest(request.roomName,
					request.masterPlayer,
					message);

			mediator.Send(publishRequest);

			return Task.FromResult(MediatR.Unit.Value);
		}
	}
}