using MediatR;
using RabbitGameServer;
using RabbitGameServer.Client;
using RabbitGameServer.Database;
using RabbitGameServer.Game;
using RabbitGameServer.Service;
using RabbitGameServer.SharedModel;
using RabbitGameServer.Util;

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

// to use Development settings (appsettings.Development.json)
// $ export DOTNET_ENVIRONMENT Development
// similar, to use production settings (appsettings.Production.json)
// $ export DOTNET_ENVIRONMENT Development

var host = builder.Build();

await host.RunAsync();
