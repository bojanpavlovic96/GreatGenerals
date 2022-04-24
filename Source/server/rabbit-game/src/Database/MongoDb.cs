using MongoDB.Driver;

namespace RabbitGameServer.Database
{
	public class MongoDb : IDatabase
	{

		// TODO extract this to a config obj/file
		private string mongoUrl;
		private string mongoUser;
		private string mongoPassword;
		private string databaseName;
		private string gamesCollection;

		private string MongoUri => "mongodb://" + mongoUser + ":" + mongoPassword
				+ "@" + mongoUrl;

		private MongoClient client;
		private IMongoDatabase database;

		public MongoDb(string mongoUrl,
			string mongoUser,
			string mongoPassword,
			string databaseName,
			string gamesCollection)
		{
			this.mongoUrl = mongoUrl;
			this.mongoUser = mongoUser;
			this.mongoPassword = mongoPassword;
			this.databaseName = databaseName;
			this.gamesCollection = gamesCollection;

			// I don't think this field is actually necessary
			client = new MongoClient(mongoUrl);
			database = client.GetDatabase(databaseName);
		}

		public void saveCommand(string gameId, Command command)
		{
			var filter = (Game g) => g.id == new MongoDB.Bson.ObjectId(gameId);
			var update = Builders<Game>
				.Update
				.Push<Command>(g => g.commands, command);

			database.GetCollection<Game>(gamesCollection)
				.UpdateOne(
					g => g.id == new MongoDB.Bson.ObjectId(gameId),
					update);
		}

		public string saveGame(string roomName, List<string> players)
		{
			var game = new Game(roomName, players);

			database.GetCollection<Game>(gamesCollection)
				.InsertOne(game);

			return game.id.ToString();
		}
	}
}