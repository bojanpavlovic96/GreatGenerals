using Microsoft.Extensions.Options;
using RabbitGameServer.Service;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitMQ.Client;

namespace RabbitGameServer.Client
{
	public class RabbitPlayerProxy : IPlayerProxy, IDisposable
	{

		private QueuesConfig config;
		private IRabbitConnection connection;
		private IProtocolTranslator translator;

		private IModel channel;

		public RabbitPlayerProxy(IOptions<QueuesConfig> options,
						IRabbitConnection rabbitConnection,
						IProtocolTranslator translator)
		{
			this.config = options.Value;
			this.connection = rabbitConnection;
			this.translator = translator;

			channel = connection.GetChannel();

		}

		public void sendResponse(string roomName, string playerId, Message newMessage)
		{

			// already done in constructor
			// channel = connection.GetChannel();

			var byteContent = translator.ToByteData(newMessage);

			channel.ExchangeDeclare(config.RoomsResponseTopic, "topic",
								false,
								true,
								null);

			var topic = config.RoomsResponseTopic;
			var route = responseRoutingKeyFor(roomName, playerId);
			Console.WriteLine($"Publishing on: {topic} - {route}");

			try
			{
				channel.BasicPublish(
					config.RoomsResponseTopic,
					responseRoutingKeyFor(roomName, playerId),
					null,
					byteContent);
			}
			catch (Exception e)
			{
				Console.WriteLine("Failed to publish response message ... ");
				Console.WriteLine(e.Message);
			}

		}

		private string responseRoutingKeyFor(string roomName, string playerId)
		{
			return $"{config.RoomResponseRoute}{roomName}.{playerId}";
		}


		public void Dispose()
		{
			if (channel != null)
			{
				channel.Close();
			}
		}

		public void sendUpdate(string roomName, string player, Message update)
		{
			var byteContent = translator.ToByteData(update);

			channel.ExchangeDeclare(config.RoomsResponseTopic, "topic",
								false,
								true,
								null);

			var topic = config.RoomsResponseTopic;
			var route = updateRoutingKeyFor(roomName, player);
			Console.WriteLine($"Publishing on: {topic} - {route}");

			try
			{
				channel.BasicPublish(
					config.RoomsResponseTopic,
					responseRoutingKeyFor(roomName, player),
					null,
					byteContent);
			}
			catch (Exception e)
			{
				Console.WriteLine("Failed to publish update message ... ");
				Console.WriteLine(e.Message);
			}
		}

		private string updateRoutingKeyFor(string roomName, string player)
		{
			return $"{config.RoomUpdateRoute}{roomName}.{player}";
		}

	}
}