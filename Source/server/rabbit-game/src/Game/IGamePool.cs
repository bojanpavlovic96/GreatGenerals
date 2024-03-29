using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Game
{
	public interface IGamePool
	{
		GameMaster CreateGame(string roomName, string password, PlayerData master);

		GameMaster GetGame(string roomName);

		bool destroyGame(String roomName);

		PoolSummary GetPoolSummary();

		List<GameSummary> GetGameSummaries();

		GameSummary GetGameSummary(string room);

		GameMaster LoadReplay(string roomId);
	}

}
