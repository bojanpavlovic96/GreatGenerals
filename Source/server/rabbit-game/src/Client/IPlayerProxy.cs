using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Client
{
	public interface IPlayerProxy
	{
		void sendRoomResponse(string roomName, string playerId, Message response);

		void sendRoomUpdate(string roomName, string player, Message update);

		void sendMessage(string roomName, string player, Message command);
	}
}