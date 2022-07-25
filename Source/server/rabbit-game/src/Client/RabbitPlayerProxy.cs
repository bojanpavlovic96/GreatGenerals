using Microsoft.Extensions.Options;
using RabbitGameServer.Service;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.Util;
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

		public void sendMessage(string roomName, string playerId, Message newMessage)
		{

			// already done in constructor
			// channel = connection.GetChannel();

			Console.WriteLine("Channel created inside proxy ... ");
			var byteContent = translator.ToByteData(newMessage);

			channel.ExchangeDeclare(config.RoomsResponseTopic, "topic",
								false,
								true,
								null);
			Console.WriteLine($"Publishing on: {config.RoomsResponseTopic} - {routingKeyFor(roomName, playerId)}");

			try
			{
				channel.BasicPublish(
					config.RoomsResponseTopic,
					routingKeyFor(roomName, playerId),
					null,
					byteContent);
			}
			catch (Exception e)
			{
				Console.WriteLine("Failed to publish message ... ");
				Console.WriteLine(e.Message);
			}

		}

		private string routingKeyFor(string roomName, string playerId)
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

	}
}