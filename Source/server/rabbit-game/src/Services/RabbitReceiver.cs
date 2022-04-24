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
		private IRabbitConnection rabbitConnection;
		// private RabbitConfig rabbitConfig;
		private QueuesConfig queuesConfig;
		private INameTypeMapper typeMapper;
		private ISerializer serializer;


		// private IConnection connection;
		private IModel modelEventChannel;
		private IModel newGameChannel;

		private CancellationToken masterToken;

		public RabbitReceiver(IMediator mediator,
			IRabbitConnection rabbitConnection,
			// RabbitConfig rabbitConfig,
			IOptions<QueuesConfig> queuesConfig,
			INameTypeMapper typeMapper,
			ISerializer serializer)
		{
			this.mediator = mediator;

			this.rabbitConnection = rabbitConnection;

			// this.rabbitConfig = rabbitConfig;
			this.queuesConfig = queuesConfig.Value;
			this.typeMapper = typeMapper;
			this.serializer = serializer;
		}

		protected override async Task ExecuteAsync(CancellationToken stoppingToken)
		{
			// masterToken = stoppingToken;

			// var factory = new ConnectionFactory();
			// factory.HostName = rabbitConfig.HostName;
			// factory.Port = rabbitConfig.Port;
			// factory.VirtualHost = rabbitConfig.VHost;
			// factory.UserName = rabbitConfig.UserName;
			// factory.Password = rabbitConfig.Password;

			// connection = factory.CreateConnection();

			setupNewGameEventConsumer();

			setupModelEventConsumer();
		}

		private void setupNewGameEventConsumer()
		{
			// newGameChannel = connection.CreateModel();
			newGameChannel = rabbitConnection.GetChannel();

			newGameChannel.ExchangeDeclare(queuesConfig.NewGameTopic,
								"topic",
								false,
								true,
								null);

			var newGameQueue = newGameChannel.QueueDeclare().QueueName;

			newGameChannel.QueueBind(newGameQueue,
						queuesConfig.NewGameTopic,
						queuesConfig.NewGameRoute,
						null);

			var consumer = new EventingBasicConsumer(newGameChannel);
			consumer.Received += NewGameEventHandler;

			newGameChannel.BasicConsume(newGameQueue, false, consumer);
		}

		private void NewGameEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			var body = ea.Body;
			var props = ea.BasicProperties;

			var message = serializer.ToObj<NewGameRequest>(body.ToArray());

			mediator.Send(message, masterToken).Wait();
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
						queuesConfig.ModelEventRoute,
						null);

			var consumer = new EventingBasicConsumer(modelEventChannel);
			consumer.Received += ModelEventHandler;

			modelEventChannel.BasicConsume(modelEventQueue, false, consumer);

			// var consumer = new ModelEventConsumer();

			// var consumer = new EventingBasicConsumer(modelEventChannel);
			// consumer.Received += (channel, eventArg) =>
			// {
			// 	var strBody = Encoding.UTF8.GetString(eventArg.Body.ToArray());
			// 	NamedWrapper wrappedMessage =
			// 		(NamedWrapper)serializer.ToObj(strBody, typeof(NamedWrapper));


			// 	Type messageType = typeMapper.GetType(wrappedMessage.name);

			// 	ModelEvent modelEvent =
			// 		(ModelEvent)serializer.ToObj(strBody, messageType);

			// };
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