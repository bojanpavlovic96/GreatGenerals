using MediatR;
using RabbitGameServer;
using RabbitGameServer.Client;
using RabbitGameServer.Database;
using RabbitGameServer.Game;
using RabbitGameServer.Service;
using RabbitGameServer.SharedModel;
using RabbitGameServer.Util;

// const string RABBIT_CONFIG_SECTION = "RabbitConfig";
// const string QUEUES_CONFIG_SECTION = "QueuesConfig";
// const string MONGO_CONFIG_SECTION = "MongoConfig";

var builder = Host.CreateDefaultBuilder(args);

builder.ConfigureServices((hostContext, services) =>
{
	services.Configure<RabbitConfig>(
		hostContext.Configuration.GetSection(RabbitConfig.ConfigSection));

	services.Configure<QueuesConfig>(
		hostContext.Configuration.GetSection(QueuesConfig.ConfigSection));

	services.Configure<MongoConfig>(
		hostContext.Configuration.GetSection(MongoConfig.ConfigSection));


	services.AddMediatR(typeof(Program).Assembly);

	services.AddSingleton<IGamePool, GamePool>();
	services.AddSingleton<IRabbitConnection, RabbitConnection>();

	services.AddTransient<ISerializer, NSoftSerializer>();
	services.AddTransient<IDatabase, MongoDb>();
	services.AddTransient<INameTypeMapper, StupidStaticTypeMapper>();
	services.AddTransient<IProtocolTranslator, NamedWrapperTranslator>();
	services.AddTransient<IPlayerProxy, RabbitPlayerProxy>();

	services.AddHostedService<RabbitReceiver>();

});

var host = builder.Build();

await host.RunAsync();
