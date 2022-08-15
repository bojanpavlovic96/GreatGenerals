namespace RabbitGameServer
{

	public class RabbitConfig
	{

		public static string ConfigSection = "RabbitConfig";

		public string? HostName { get; set; }
		public int? Port { get; set; }
		public string? UserName { get; set; }
		public string? Password { get; set; }
		public string? VHost { get; set; }

	}
}