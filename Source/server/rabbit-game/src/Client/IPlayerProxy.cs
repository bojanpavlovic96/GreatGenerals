using RabbitGameServer.SharedModel.Commands;

namespace RabbitGameServer.Client
{
	public interface IPlayerProxy
	{
		void sendCommand(string roomName, string playerId, Command newCommand);
	}
}