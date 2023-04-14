namespace RabbitGameServer.SharedModel.ClientIntentions
{

	public class ReadyForInitIntention : ClientIntention
	{

		public String roomName { get; set; }

		public ReadyForInitIntention(string playerName, String roomName)
			: base(ClientIntentionType.ReadyForInit, playerName)
		{
			this.roomName = roomName;
		}
	}
}