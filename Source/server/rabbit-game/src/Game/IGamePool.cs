namespace RabbitGameServer.Game
{
	public interface IGamePool
	{
		GameMaster CreateGame(string roomName, string masterPlayer);

		GameMaster GetGame(string roomName);

		BasicStats GetBasicPoolStats();

	}

}
