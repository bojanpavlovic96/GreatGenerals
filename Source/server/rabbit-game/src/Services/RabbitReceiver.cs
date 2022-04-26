using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Mediator;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.ModelEvents;
using RabbitGameServer.Util;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace RabbitGameServer.Service
{

	public class RabbitReceiver : BackgroundService
	{

		private IMediator mediator;
		private QueuesConfig queuesConfig;
		private IRabbitConnection rabbitConnection;
		private INameTypeMapper typeMapper;
		private ISerializer serializer;

		private IModel modelEventChannel;
		private IModel newGameChannel;

		private CancellationToken masterToken;

		public RabbitReceiver(IMediator mediator,
			IRabbitConnection rabbitConnection,
			IOptions<QueuesConfig> queuesConfig,
			ISerializer serializer)
		{

			this.mediator = mediator;

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

			var message = serializer.ToObj<NewGameRequest>(ea.Body.ToArray());

			var request = new CreateGameRequest(message.roomName, message.players);
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
			var body = ea.Body;
			var modelEvent = serializer.ToObj<ModelEvent>(body.ToArray());

			var request = new ModelEventRequest(modelEvent);
			mediator.Send(request, masterToken).Wait();
		}

	}

}