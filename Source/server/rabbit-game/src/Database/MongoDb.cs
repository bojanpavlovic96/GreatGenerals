using Microsoft.Extensions.Options;
using MongoDB.Driver;
using RabbitGameServer.Config;

namespace RabbitGameServer.Database
{
	public class MongoDb : IDatabase
	{

		private MongoConfig config;

		private string MongoUri =>
			"mongodb://" + config.MongoUser + ":" + config.MongoPassword
			+ "@" + config.MongoUrl;

		private MongoClient client;
		private IMongoDatabase database;

		public MongoDb(IOptions<MongoConfig> options)
		{

			this.config = options.Value;

			// I don't think this field is actually necessary
			client = new MongoClient(MongoUri);
			database = client.GetDatabase(config.DatabaseName);
		}

		public void saveCommand(string gameId, Message command)
		{
			var filter = (Game g) => g.id == new MongoDB.Bson.ObjectId(gameId);
			var update = Builders<Game>
				.Update
				.Push<Message>(g => g.messages, command);

			database.GetCollection<Game>(config.GamesCollection)
				.UpdateOne(
					g => g.id == new MongoDB.Bson.ObjectId(gameId),
					update);
		}

		public string saveGame(string roomName, List<string> players)
		{
			var game = new Game(roomName, players);

			database.GetCollection<Game>(config.GamesCollection)
				.InsertOne(game);

			return game.id.ToString();
		}
	}
}