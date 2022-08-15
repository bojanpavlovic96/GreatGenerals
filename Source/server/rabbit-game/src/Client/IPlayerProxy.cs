using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Client
{
	public interface IPlayerProxy
	{
		void sendResponse(string roomName, string playerId, Message newMessage);

		void sendUpdate(string roomName, string player, Message update);
	}
}