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

			return;

			var byteContent = translator.ToByteData(newMessage);

			channel.ExchangeDeclare(config.RoomsResponseTopic, "topic",
								false,
								true,
								null);

			var topic = config.RoomsResponseTopic;
			var route = formResponseRoute(roomName, playerId);
			Console.WriteLine($"Publishing on: {topic} - {route}");

			try
			{
				channel.BasicPublish(config.RoomsResponseTopic,
					formResponseRoute(roomName, playerId),
					null,
					byteContent);
			}
			catch (Exception e)
			{
				Console.WriteLine("Failed to publish response message ... ");
				Console.WriteLine(e.Message);
			}

		}

		private string formResponseRoute(string roomName, string playerId)
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

		private string formUpdateRoute(string roomName, string player)
		{
			return $"{config.RoomUpdateRoute}{roomName}.{player}";
		}

		public void sendMessage(string roomName, string player, Message message)
		{

			try
			{
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

		private string formMessageRoute(string room, string player)
		{
			return $"{config.ServerMessageRoutePrefix}{room}.{player}";
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