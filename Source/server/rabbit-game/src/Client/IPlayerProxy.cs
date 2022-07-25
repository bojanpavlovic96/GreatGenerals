using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Client
{
	public interface IPlayerProxy
	{
		void sendMessage(string roomName, string playerId, Message newMessage);
	}
}