using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Mediator;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.Util;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace RabbitGameServer.Service
{

	public class RabbitReceiver : BackgroundService
	{

		private QueuesConfig queuesConfig;

		private IMediator mediator;

		private IProtocolTranslator protocolTranslator;
		private ISerializer serializer;

		private IRabbitConnection rabbitConnection;
		private IModel? modelEventChannel;
		private IModel? roomChannel;

		private CancellationToken masterToken;

		public RabbitReceiver(IMediator mediator,
			IProtocolTranslator protocolTranslator,
			IRabbitConnection rabbitConnection,
			IOptions<QueuesConfig> queuesConfig,
			ISerializer serializer)
		{

			this.mediator = mediator;

			this.protocolTranslator = protocolTranslator;
			this.rabbitConnection = rabbitConnection;

			this.queuesConfig = queuesConfig.Value;
			this.serializer = serializer;

		}

		protected override async Task ExecuteAsync(CancellationToken stoppingToken)
		{
			masterToken = stoppingToken; // might be useful ... you never know 

			setupNewRoomEventConsumer();
			setupJoinGameConsumer();
			setupModelEventConsumer();

			Console.WriteLine("RabbitReceiver started");
		}

		private void setupNewRoomEventConsumer()
		{
			roomChannel = rabbitConnection.GetChannel();

			roomChannel.ExchangeDeclare(queuesConfig.RoomsRequestTopic,
								"topic",
								false,
								true,
								null);

			var newGameQueue = roomChannel.QueueDeclare().QueueName;

			roomChannel.QueueBind(newGameQueue,
						queuesConfig.RoomsRequestTopic,
						queuesConfig.NewGameRoute + "#",
						null);

			var consumer = new EventingBasicConsumer(roomChannel);
			consumer.Received += NewGameEventHandler;

			roomChannel.BasicConsume(newGameQueue, false, consumer);

			Console.WriteLine("NewGameEventConsumer started");
		}

		private void NewGameEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			Console.WriteLine("Received new game event");
			var message = protocolTranslator.ToMessage(ea.Body.ToArray());

			var roomMsg = (CreateRoomMsg)message;
			var request = new CreateRoomRequest(roomMsg.roomName,
								roomMsg.password,
								roomMsg.player);

			mediator.Send(request);
		}

		private void setupModelEventConsumer()
		{
			modelEventChannel = rabbitConnection.GetChannel();

			modelEventChannel.ExchangeDeclare(queuesConfig.ModelEventTopic,
								"topic",
								false,
								true,
								null);

			var modelEventQueue = modelEventChannel.QueueDeclare().QueueName;

			modelEventChannel.QueueBind(modelEventQueue,
						queuesConfig.ModelEventTopic,
						queuesConfig.ModelEventRoute + "*",
						null);

			var consumer = new EventingBasicConsumer(modelEventChannel);
			consumer.Received += ModelEventHandler;

			modelEventChannel.BasicConsume(modelEventQueue, false, consumer);

			Console.WriteLine("ModelEventConsumer started");

		}

		private void ModelEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			var message = protocolTranslator.ToMessage(ea.Body.ToArray());

			var request = new ModelEventRequest(message);
			mediator.Send(request, masterToken).Wait();
		}

		private void setupJoinGameConsumer()
		{
			var joinGameQueue = roomChannel.QueueDeclare().QueueName;
			roomChannel.QueueBind(joinGameQueue,
						queuesConfig.RoomsRequestTopic,
						queuesConfig.JoinGameRoute + "*",
						null);

			var consumer = new EventingBasicConsumer(roomChannel);
			consumer.Received += JoinGameEventHandler;
		}

		private void JoinGameEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			Console.WriteLine("Received join room event... ;");

			var message = protocolTranslator.ToMessage(ea.Body.ToArray());

			var request = new JoinGameRequest(message.roomName,
					message.player,
					((JoinRoomMessage)message).password);

			mediator.Send(request);
		}

	}

}