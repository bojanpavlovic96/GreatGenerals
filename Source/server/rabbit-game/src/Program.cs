using MediatR;
using RabbitGameServer;
using RabbitGameServer.Database;
using RabbitGameServer.Game;
using RabbitGameServer.Service;
using RabbitGameServer.Util;

const string RABBIT_CONFIG_SECTION = "RabbitConfig";
const string QUEUES_CONFIG_SECTION = "QueuesConfig";
const string MONGO_CONFIG_SECTION = "MongoConfig";

var builder = Host.CreateDefaultBuilder(args);

builder.ConfigureServices((hostContext, services) =>
{
	services.Configure<RabbitConfig>(
		hostContext.Configuration.GetSection(RABBIT_CONFIG_SECTION));

	services.Configure<QueuesConfig>(
		hostContext.Configuration.GetSection(QUEUES_CONFIG_SECTION));

	services.Configure<MongoConfig>(
		hostContext.Configuration.GetSection(MONGO_CONFIG_SECTION));


	services.AddMediatR(typeof(Program).Assembly);

	services.AddSingleton<IGamePool, GamePool>();
	services.AddSingleton<IRabbitConnection, RabbitConnection>();

	services.AddTransient<ISerializer, NSoftSerializer>();
	services.AddTransient<IDatabase, MongoDb>();
	services.AddTransient<INameTypeMapper, StupidStaticTypeMapper>();

	services.AddHostedService<RabbitReceiver>();

});

var host = builder.Build();

await host.RunAsync();

// // TODO extract to config 
// const string MONGO_URL = "mongodb://localhost";
// const string MONGO_USER = "gg_user";
// const string MONGO_PASSWORD = "gg_password";

// const string DATABASE_NAME = "gg_games";
// const string GAMES_COLLECTION = "game_info";

// const string SERVER_COMMANDS_TOPIC = "gg_server_commands";

// // TODO extract to config
// var NEW_GAME_TOPIC = "new-game-topic";
// var NEW_GAME_ROUTE = "new-game-route";

// var database = new MongoDb(MONGO_URL,
// 					MONGO_USER,
// 					MONGO_PASSWORD,
// 					DATABASE_NAME,
// 					GAMES_COLLECTION);

// var serializer = new NSoftSerializer();

// var playerProxy = new RabbitPlayerProxy(
// 	SERVER_COMMANDS_TOPIC,
// 	RabbitConnection.Instance.getChannel(),
// 	serializer);

// GamePool pool = new GamePool(database, playerProxy);

// var channel = RabbitConnection.Instance.getChannel();

// channel.ExchangeDeclare(NEW_GAME_TOPIC, "topic", false, true, null);
// var newGameQueueName = channel.QueueDeclare().QueueName;
// channel.QueueBind(newGameQueueName, NEW_GAME_TOPIC, NEW_GAME_ROUTE);

// var newGameConsumer = new NewGameConsumer(pool);
// channel.BasicConsume(newGameQueueName, true, newGameConsumer);

// #region catch ctrl+c

// // prevent main from exiting
// Console.WriteLine("Server started ... ");
// Console.WriteLine("Press Ctrl+C to shut down server ... ");
// var exitEvent = new AutoResetEvent(false);
// Console.CancelKeyPress += (s, e) => { e.Cancel = true; exitEvent.Set(); };
// exitEvent.WaitOne();
// Console.WriteLine("");
// Console.WriteLine("Ctrl+C pressed.");

// #endregion

// RabbitConnection.Instance.Close();