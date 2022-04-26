using Microsoft.Extensions.Options;
using RabbitGameServer.Service;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Commands;
using RabbitGameServer.Util;
using RabbitMQ.Client;

namespace RabbitGameServer.Client
{
	public class RabbitPlayerProxy : IPlayerProxy
	{

		private QueuesConfig config;
		private IRabbitConnection connection;
		private ISerializer commandSerializer;

		public RabbitPlayerProxy(IOptions<QueuesConfig> options,
						IRabbitConnection rabbitConnection,
						ISerializer serializer)
		{
			this.config = options.Value;
			this.connection = rabbitConnection;
			this.commandSerializer = serializer;
		}

		public void sendCommand(string roomName, string playerId, Command newCommand)
		{

			var channel = connection.GetChannel();

			var wrapper = new NamedWrapper(
				newCommand.name,
				commandSerializer.ToString(newCommand));

			var byteContent = commandSerializer.ToBytes(wrapper);

			channel.BasicPublish(
				config.ServerCommandTopic,
				routingKeyFor(roomName, playerId),
				null,
				byteContent);
		}

		private string routingKeyFor(string roomName, string playerId)
		{

			var route = config.ServerCommandRoutePrefix + "." + roomName;
			if (playerId != null && playerId != "")
			{
				route += "." + playerId;
			}

			return route;
		}

	}
}