namespace RabbitGameServer.Config
{
	public class LoginServerConfig
	{

		public static string ConfigSection = "LoginServer";

		public string Address { get; set; }
		public int Port { get; set; }
		public string GetPlayerPath { get; set; }
		public string NameArgument { get; set; }

		public string UpdatePlayerPath { get; set; }

	}
}