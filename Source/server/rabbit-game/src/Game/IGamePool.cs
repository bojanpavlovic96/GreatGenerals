namespace RabbitGameServer.Game
{
	public interface IGamePool
	{
		GameMaster CreateGame(string roomName, string masterPlayer, string password);

		GameMaster GetGame(string roomName);

		void destroyGame(String roomName);

		PoolSummary GetPoolSummary();

		List<GameSummary> GetGameSummaries();

		GameSummary GetGameSummary(string room);
	}

}
