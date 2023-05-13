namespace RabbitGameServer.Database
{
	public interface IDatabase
	{
		void saveMessages(string gameId, List<Message> messages);

		string saveGame(string roomName, string masterPlayer, List<string> players);

		List<DbGame> getGames(string user);

		List<Message> getMessages(string roomId);

	}
}