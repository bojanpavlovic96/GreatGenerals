
using MediatR;
using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Mediator
{
	public class UpdatePlayerRequest : IRequest<PlayerData>
	{
		public PlayerData playerData;

		public UpdatePlayerRequest(PlayerData playerData)
		{
			this.playerData = playerData;
		}
	}
}