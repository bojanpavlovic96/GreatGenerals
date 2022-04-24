namespace RabbitGameServer.Game
{
	public interface IGamePool
	{
		GameMaster CreateGame(string roomName, List<string> players);

		GameMaster GetGame(string roomName);

		BasicStats GetBasicPoolStats();

	}

}
