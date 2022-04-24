using RabbitGameServer.SharedModel.Commands;

namespace RabbitGameServer.Database
{
	public interface IDatabase
	{
		void saveCommand(string gameId, Command command);

		string saveGame(string roomName, List<string> players);

	}
}