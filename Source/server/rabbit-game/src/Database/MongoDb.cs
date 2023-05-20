using Microsoft.Extensions.Options;
using MongoDB.Bson;
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

			var creds = MongoCredential.CreateCredential(
				config.DatabaseName,
				config.MongoUser,
				config.MongoPassword);

			var settings = new MongoClientSettings()
			{
				Credential = creds,
				Server = new MongoServerAddress(config.MongoUrl)
			};

			client = new MongoClient(settings);
			database = client.GetDatabase(config.DatabaseName);
		}

		public void saveMessages(string roomId, List<Message> messages)
		{
			var filter = Builders<MsgContainer>
				.Filter
				.Eq<string>(mc => mc.roomId, roomId);

			var update = Builders<MsgContainer>
				.Update
				.PushEach<Message>(c => c.messages, messages);

			database
				.GetCollection<MsgContainer>(config.MessagesCollections)
				.UpdateOne(filter, update);

		}

		public string saveGame(string roomName,
			string masterPlayer,
			List<string> players,
			DateTime startedAt)
		{
			DbGame game = new DbGame(roomName, masterPlayer, players, startedAt);
			database
				.GetCollection<DbGame>(config.GamesCollection)
				.InsertOne(game);

			var container = new MsgContainer(game._id.ToString());
			database
				.GetCollection<MsgContainer>(config.MessagesCollections)
				.InsertOne(container);

			return game._id.ToString();
		}

		public List<DbGame> getGames(string user)
		{
			return database
					.GetCollection<DbGame>(config.GamesCollection)
					.Find<DbGame>((dbGame) => dbGame.players.Contains(user))
					.ToList<DbGame>();
		}

		public List<Message> getMessages(string roomId)
		{
			return database
				.GetCollection<MsgContainer>(config.MessagesCollections)
				.Find((msgC) => msgC.roomId == roomId)
				.First()
				.messages
				.OrderBy(m => m.timestamp)
				.Select((m) => { Console.WriteLine(m); return m; }) // TODO remove
				.ToList();
		}

		public DbGame getGame(string roomId)
		{
			var filter = Builders<DbGame>
				.Filter
				.Where((dbg) => dbg._id == new ObjectId(roomId));
			return database
				.GetCollection<DbGame>(config.GamesCollection)
				.Find<DbGame>((dbg) => dbg._id == new ObjectId(roomId))
				// .Find<DbGame>((game) => game._id.ToString() == roomId)
				.First();
		}
	}
}