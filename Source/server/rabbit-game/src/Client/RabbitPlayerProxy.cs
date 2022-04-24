using RabbitGameServer.SharedModel.Commands;
using RabbitGameServer.Util;
using RabbitMQ.Client;

namespace RabbitGameServer.Client
{
	public class RabbitPlayerProxy : IPlayerProxy
	{

		private RabbitConfig config;

		private string TopicName;

		private IModel Channel;

		private ISerializer commandSerializer;

		public RabbitPlayerProxy(string topicName,
						IModel channel,
						ISerializer serializer)
		{
			this.TopicName = topicName;
			this.Channel = channel;
			this.commandSerializer = serializer;
		}

		public void sendCommand(string roomName, string playerId, Command newCommand)
		{
			// TODO wrap this command first ... 
			var byteContent = commandSerializer.ToBytes(newCommand);
			Channel.BasicPublish(
				TopicName,
				routingKeyFor(roomName, playerId),
				null,
				byteContent);
		}

		private string routingKeyFor(string roomName, string playerId)
		{
			// return ROUTING_KEY_PREFIX + "." + roomName + "" + playerId;
			return "";
		}

	}
}