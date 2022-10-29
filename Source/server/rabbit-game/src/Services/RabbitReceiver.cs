using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Mediator;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace RabbitGameServer.Service
{

	public class RabbitReceiver : BackgroundService
	{

		private QueuesConfig queuesConfig;

		private IMediator mediator;

		private IProtocolTranslator protocolTranslator;

		private IRabbitConnection rabbitConnection;
		private IModel? modelEventChannel;
		private IModel? roomChannel;

		private CancellationToken masterToken;

		public RabbitReceiver(IMediator mediator,
			IProtocolTranslator protocolTranslator,
			IRabbitConnection rabbitConnection,
			IOptions<QueuesConfig> queuesConfig)
		{

			this.mediator = mediator;

			this.protocolTranslator = protocolTranslator;
			this.rabbitConnection = rabbitConnection;

			this.queuesConfig = queuesConfig.Value;

		}

		protected override async Task ExecuteAsync(CancellationToken stoppingToken)
		{
			masterToken = stoppingToken; // might be useful ... you never know 

			await Task.Run(() =>
			{
				roomChannel = rabbitConnection.GetChannel();
				modelEventChannel = rabbitConnection.GetChannel();

				if (roomChannel == null || modelEventChannel == null)
				{
					Console.WriteLine("Failed to create channels for rabbit receiver ... ");
					return;
				}

				setupNewRoomEventConsumer();
				setupJoinRoomConsumer();
				setupLeaveRoomConsumer();
				setupStartGameConsumer();

				setupModelEventConsumer();

				Console.WriteLine("RabbitReceiver started");
			});

		}

		private void setupNewRoomEventConsumer()
		{
			roomChannel.ExchangeDeclare(queuesConfig.RoomsRequestTopic,
								"topic",
								false,
								true,
								null);

			var newGameQueue = roomChannel.QueueDeclare().QueueName;

			roomChannel.QueueBind(newGameQueue,
						queuesConfig.RoomsRequestTopic,
						queuesConfig.NewRoomRoute + queuesConfig.MatchAllWildcard,
						null);

			var consumer = new EventingBasicConsumer(roomChannel);
			consumer.Received += NewGameEventHandler;

			roomChannel.BasicConsume(newGameQueue, false, consumer);

			Console.WriteLine("NewRoomEventConsumer started");

			return;
		}

		private void NewGameEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			var message = protocolTranslator.ToMessage(ea.Body.ToArray());
			Console.WriteLine("Received new game event");

			var roomMsg = (CreateRoomMsg)message;
			var request = new CreateRoomRequest(roomMsg.roomName,
								roomMsg.password,
								roomMsg.username);

			mediator.Send(request);
		}

		private void setupModelEventConsumer()
		{

			modelEventChannel.ExchangeDeclare(queuesConfig.ModelEventTopic,
								"topic",
								false,
								true,
								null);

			var modelEventQueue = modelEventChannel.QueueDeclare().QueueName;

			modelEventChannel.QueueBind(modelEventQueue,
						queuesConfig.ModelEventTopic,
						queuesConfig.ModelEventRoute + queuesConfig.MatchAllWildcard,
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
			// I guess .Wait is so that all events are processed and executed in the
			// order they were received but since there is single channel/receiver
			// for all active games this is gonna be major bottleneck I would assume
			// even with only two active games ... ? 
		}

		private void setupJoinRoomConsumer()
		{
			var joinRoomQueue = roomChannel.QueueDeclare().QueueName;
			roomChannel.QueueBind(joinRoomQueue,
						queuesConfig.RoomsRequestTopic,
						queuesConfig.JoinRoomRoute + queuesConfig.MatchAllWildcard,
						null);

			var consumer = new EventingBasicConsumer(roomChannel);
			consumer.Received += JoinRoomEventHandler;

			roomChannel.BasicConsume(joinRoomQueue, false, consumer);

			Console.WriteLine($"JoinRoomEventConsumer started ... ");
			Console.WriteLine($"{queuesConfig.RoomsRequestTopic} - {queuesConfig.JoinRoomRoute}{queuesConfig.MatchAllWildcard}");

			return;
		}

		private void JoinRoomEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			Console.WriteLine("Received join room event... ;");

			var message = protocolTranslator.ToMessage(ea.Body.ToArray());

			var request = new JoinRoomRequest(message.roomName,
					message.username,
					((JoinRoomMessage)message).password);

			mediator.Send(request);
		}

		private void setupLeaveRoomConsumer()
		{
			var leaveRoomQueue = roomChannel.QueueDeclare().QueueName;
			roomChannel.QueueBind(leaveRoomQueue,
						queuesConfig.RoomsRequestTopic,
						queuesConfig.LeaveRoomRoute + queuesConfig.MatchAllWildcard,
						null);

			var consumer = new EventingBasicConsumer(roomChannel);
			consumer.Received += LeaveRoomEventHandler;

			roomChannel.BasicConsume(leaveRoomQueue, false, consumer);

			Console.WriteLine($"LeaveRoomEventConsumer started ... ");
			Console.WriteLine($"{queuesConfig.RoomsRequestTopic} - {queuesConfig.LeaveRoomRoute}{queuesConfig.MatchAllWildcard}");

			return;
		}

		private void LeaveRoomEventHandler(object? sender, BasicDeliverEventArgs ea)
		{
			Console.WriteLine("Received leave room event ... ");

			var message = protocolTranslator.ToMessage(ea.Body.ToArray());
			var request = new LeaveRoomRequest(message.roomName, message.username);

			mediator.Send(request);
		}

		private void setupStartGameConsumer()
		{
			var startGameQueue = roomChannel.QueueDeclare().QueueName;

			roomChannel.QueueBind(startGameQueue,
						queuesConfig.RoomsRequestTopic,
						queuesConfig.StartGameRoute + queuesConfig.MatchAllWildcard,
						null);
			var consumer = new EventingBasicConsumer(roomChannel);
			consumer.Received += StartGameEventHandler;

			roomChannel.BasicConsume(startGameQueue, false, consumer);

			Console.WriteLine($"StartGameEventConsumer started ... ");
			Console.WriteLine($"{queuesConfig.RoomsRequestTopic} - {queuesConfig.StartGameRoute}{queuesConfig.MatchAllWildcard}");

			return;
		}

		private void StartGameEventHandler(object? sender, BasicDeliverEventArgs ea)
		{

			Console.WriteLine("Received start game room request ... ");

			var message = protocolTranslator.ToMessage(ea.Body.ToArray());
			var request = new StartGameRequest(message.roomName, message.username);

			mediator.Send(request);

		}

	}

}