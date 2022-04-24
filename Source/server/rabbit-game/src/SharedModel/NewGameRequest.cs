namespace RabbitGameServer.SharedModel
{

	public class NewGameRequest
	{

		public string roomName { get; set; }

		public List<string> players { get; set; }

	}

}