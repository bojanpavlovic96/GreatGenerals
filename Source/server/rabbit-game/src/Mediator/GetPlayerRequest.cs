using MediatR;
using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Mediator
{
	public class GetPlayerRequest : IRequest<PlayerData>
	{
		public string name { get; set; }

		public GetPlayerRequest(string name)
		{
			this.name = name;
		}
	}
}