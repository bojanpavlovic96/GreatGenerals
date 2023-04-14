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

			// For some reason when creating gg_user with --eval with startup script
			// user is gonna be somehow bound to test (nonexistant) database but will 
			// have all the roles given in roles[] arg ... so yeah ... hardcode test ... 
			var creds = MongoCredential.CreateCredential(
				config.DatabaseName,
				config.MongoUser,
				config.MongoPassword);

			var settings = new MongoClientSettings()
			{
				Credential = creds,
				Server = new MongoServerAddress(config.MongoUrl)
			};
			// I don't think this field is actually necessary
			// client = new MongoClient(MongoUri);
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

		public string saveGame(string roomName, string masterPlayer, List<string> players)
		{
			DbGame game = new DbGame(roomName, masterPlayer, players);
			database
				.GetCollection<DbGame>(config.GamesCollection)
				.InsertOne(game);

			var container = new MsgContainer(game._id.ToString());
			database
				.GetCollection<MsgContainer>(config.MessagesCollections)
				.InsertOne(container);

			return game._id.ToString();
		}
	}
}