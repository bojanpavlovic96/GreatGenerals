namespace RabbitGameServer.SharedModel
{
	public class NamedWrapper
	{

		public string name;
		public string payload;

		public NamedWrapper(String name, String payload)
		{
			this.name = name;
			this.payload = payload;
		}

	}
}