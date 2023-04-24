
using MediatR;

namespace RabbitGameServer.Mediator
{
	public class ListReplaysRequest : IRequest<Unit>
	{
		public string username { get; set; }

		public ListReplaysRequest(string username)
		{
			this.username = username;
		}
	}
}