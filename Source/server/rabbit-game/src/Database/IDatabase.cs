namespace RabbitGameServer.Database
{
	public interface IDatabase
	{
		void saveMessages(string gameId, List<Message> messages);

		string saveGame(string roomName, 
			string masterPlayer, 
			List<string> players, 
			DateTime startedAt);

		List<DbGame> getGames(string user);

		DbGame getGame(string roomId);

		List<Message> getMessages(string roomId);

	}
}