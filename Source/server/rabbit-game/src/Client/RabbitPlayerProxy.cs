using Microsoft.Extensions.Options;
using RabbitGameServer.Config;
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

		public void sendRoomResponse(string roomName, string playerId, Message newMessage)
		{

			try
			{
				send(newMessage,
					config.RoomsResponseTopic,
					formResponseRoute(roomName, playerId));
			}
			catch (Exception e)
			{
				Console.WriteLine("Exception while sending room response ... ");
				Console.WriteLine(e.Message);

				Console.WriteLine(e.StackTrace);
			}

		}

		private string formResponseRoute(string roomName, string playerName)
		{
			return $"{config.RoomResponseRoute}{roomName}.{playerName}";
		}


		public void Dispose()
		{
			if (channel != null)
			{
				channel.Close();
			}
		}

		public void sendRoomUpdate(string roomName, string player, Message update)
		{

			try
			{
				send(update,
					config.RoomsResponseTopic,
					formUpdateRoute(roomName, player));
			}
			catch (Exception e)
			{
				Console.WriteLine("Exception while sending room update ... ");
				Console.WriteLine(e.Message);

				Console.WriteLine(e.StackTrace);
			}

			return;

			var byteContent = translator.ToByteData(update);

			channel.ExchangeDeclare(config.RoomsResponseTopic, "topic",
								false,
								true,
								null);

			var topic = config.RoomsResponseTopic;
			var route = formUpdateRoute(roomName, player);
			Console.WriteLine($"Publishing on: {topic} - {route}");

			try
			{
				channel.BasicPublish(topic, route, null, byteContent);
			}
			catch (Exception e)
			{
				Console.WriteLine("Failed to publish update message ... ");
				Console.WriteLine(e.Message);
			}
		}

		private string formUpdateRoute(string roomName, string playerName)
		{
			return $"{config.RoomUpdateRoute}{roomName}.{playerName}";
		}

		public void sendMessage(string roomName, string player, Message message)
		{

			try
			{
				Console.WriteLine($"Proxy sending: {message.type.ToString()} .. ");
				send(message,
					config.ServerMessageTopic,
					formMessageRoute(roomName, player));
			}
			catch (Exception e)
			{
				Console.WriteLine("Exception while sending game message ... ");
				Console.WriteLine(e.Message);

				Console.WriteLine(e.StackTrace);
			}
		}

		private string formMessageRoute(string room, string playerName)
		{
			return $"{config.ServerMessageRoutePrefix}{room}.{playerName}";
		}

		// throws exception in case of an error 

		// instead of Message it could be byte[] but this way I can print 
		// adequate message using message.Type.ToString() ... 
		private void send(Message message, String topic, String route)
		{
			var byteContent = translator.ToByteData(message);

			channel.ExchangeDeclare(topic,
					"topic",
					false,
					true,
					null);

			Console.WriteLine($"Publishing: {message.type.ToString()} => {topic} => {route}");

			channel.BasicPublish(topic, route, null, byteContent);
		}

	}
}