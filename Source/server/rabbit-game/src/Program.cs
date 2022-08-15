using MediatR;
using RabbitGameServer;
using RabbitGameServer.Client;
using RabbitGameServer.Database;
using RabbitGameServer.Game;
using RabbitGameServer.Service;
using RabbitGameServer.SharedModel;
using RabbitGameServer.Util;

// to use Development settings (appsettings.Development.json)
// $ export DOTNET_ENVIRONMENT Development
// similar, to use production settings (appsettings.Production.json)
// $ export DOTNET_ENVIRONMENT Development

var wBuilder = WebApplication.CreateBuilder();

wBuilder.Services.Configure<RabbitConfig>(
	wBuilder.Configuration.GetSection(RabbitConfig.ConfigSection));

wBuilder.Services.Configure<QueuesConfig>(
	wBuilder.Configuration.GetSection(QueuesConfig.ConfigSection));

wBuilder.Services.Configure<MongoConfig>(
	wBuilder.Configuration.GetSection(MongoConfig.ConfigSection));

wBuilder.Services.AddControllers();

wBuilder.Services.AddMediatR(typeof(Program).Assembly);

wBuilder.Services.AddSingleton<IGamePool, GamePool>();
wBuilder.Services.AddSingleton<IRabbitConnection, RabbitConnection>();

wBuilder.Services.AddTransient<ISerializer, NSoftSerializer>();
wBuilder.Services.AddTransient<IDatabase, MongoDb>();
wBuilder.Services.AddTransient<INameTypeMapper, StupidStaticTypeMapper>();
wBuilder.Services.AddTransient<IProtocolTranslator, NamedWrapperTranslator>();
wBuilder.Services.AddTransient<IPlayerProxy, RabbitPlayerProxy>();

wBuilder.Services.AddHostedService<RabbitReceiver>();

var wApp = wBuilder.Build();

wApp.UseRouting();
wApp.UseEndpoints((ep) =>
{
	ep.MapControllers();
});

await wApp.RunAsync();
