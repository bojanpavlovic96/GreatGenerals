
namespace RabbitGameServer.SharedModel
{
	public enum PlayerServerResponseStatus
	{
		SUCCESS,
		// Queried by name but user doesn't exists.
		UNKNOWN_USER,
		// Queried by token but token doesn't exists.
		// This implementation does not exits but should be better than current
		// (query by name).
		INVALID_TOKEN,
		SERVER_ERROR
	}

	public class PlayerServerResponse
	{
		public PlayerServerResponseStatus status { get; set; }

		public PlayerData player { get; set; }

		public PlayerServerResponse()
		{
		}

		public PlayerServerResponse(PlayerServerResponseStatus status, PlayerData player)
		{
			this.status = status;
			this.player = player;
		}
	}
}