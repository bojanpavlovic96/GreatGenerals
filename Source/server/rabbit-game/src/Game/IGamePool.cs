namespace RabbitGameServer.Game
{
	public interface IGamePool
	{
		GameMaster CreateGame(string roomName, string masterPlayer, string password);

		GameMaster GetGame(string roomName);

		BasicStats GetBasicPoolStats();

	}

}
