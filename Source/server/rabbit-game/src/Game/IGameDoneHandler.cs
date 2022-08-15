namespace RabbitGameServer.Game
{
	public interface IGameDoneHandler
	{
		// TODO add game result ... 
		void handleGameDone(string roomName, PoolSummary gameStats);
	}
}