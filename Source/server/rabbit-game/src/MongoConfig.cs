namespace RabbitGameServer
{

	public class MongoConfig
	{

		public static string ConfigSection = "MongoConfig";

		public string MongoUrl { get; set; }

		public string MongoUser { get; set; }
		public string MongoPassword { get; set; }

		public string DatabaseName { get; set; }
		public string GamesCollection { get; set; }
	}

}