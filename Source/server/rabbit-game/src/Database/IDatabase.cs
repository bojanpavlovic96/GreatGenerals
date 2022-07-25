namespace RabbitGameServer.Database
{
	public interface IDatabase
	{
		void saveCommand(string gameId, Message command);

		string saveGame(string roomName, List<string> players);

	}
}