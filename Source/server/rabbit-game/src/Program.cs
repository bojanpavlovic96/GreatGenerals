using MediatR;
using RabbitGameServer;
using RabbitGameServer.Client;
using RabbitGameServer.Database;
using RabbitGameServer.Game;
using RabbitGameServer.Service;
using RabbitGameServer.Util;

// TODO maybe move these to static fields inside the config classes
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
	services.AddTransient<IPlayerProxy, RabbitPlayerProxy>();

	services.AddHostedService<RabbitReceiver>();

});

var host = builder.Build();

await host.RunAsync();
