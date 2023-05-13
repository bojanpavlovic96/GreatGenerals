
namespace RabbitGameServer.SharedModel
{
	public enum ReplayResponseStatus
	{
		SUCCESS,
		INVALID_NAME,
		SERVER_ERROR
	}

	public class ReplayServerResponse
	{
		public ReplayResponseStatus status{get;set;}
		public List<GameDetails> games{get;set;}

		public ReplayServerResponse(ReplayResponseStatus status, List<GameDetails> games)
		{
			this.status = status;
			this.games = games;
		}
	}
}