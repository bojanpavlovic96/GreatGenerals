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
		private IModel modelEventChannel;
		private IModel newGameChannel;

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

		protected override Task ExecuteAsync(CancellationToken stoppingToken)
		{
			masterToken = stoppingToken; // might be usefull ... you never know 

			setupNewGameEventConsumer();

			setupModelEventConsumer();

			Console.WriteLine("RabbitReceiver started");

			return Task.CompletedTask;
		}

		private void setupNewGameEventConsumer()
		{
			newGameChannel = rabbitConnection.GetChannel();

			newGameChannel.ExchangeDeclare(queuesConfig.NewGameTopic,
								"topic",
								false,
								true,
								null);

			var newGameQueue = newGameChannel.QueueDeclare().QueueName;

			newGameChannel.QueueBind(newGameQueue,
						queuesConfig.NewGameTopic,
						queuesConfig.NewGameRoute + "*",
						null);

			var consumer = new EventingBasicConsumer(newGameChannel);
			consumer.Received += NewGameEventHandler;

			newGameChannel.BasicConsume(newGameQueue, false, consumer);

			Console.WriteLine("NewGameEventConsumer started");
		}

		private void NewGameEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			Console.WriteLine("Received new game event");

			var message = protocolTranslator.ToMessage(ea.Body.ToArray());

			var request = new CreateGameRequest(
				((CreateGameMsg)message).roomName,
				((CreateGameMsg)message).player);

			mediator.Send(request);
		}

		private void setupModelEventConsumer()
		{
			// modelEventChannel = connection.CreateModel();
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

	}

}