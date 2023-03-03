namespace RabbitGameServer.SharedModel.ModelEvents
{

	public enum ClientIntentionType
	{
		Move,
		Attack,
		Defend,
		ReadyForInit,
		AbortAttack
	}

	public class ClientIntention
	{

		public ClientIntentionType type { get; set; }
		public string playerName { get; set; }

		public ClientIntention(ClientIntentionType type, string playerName)
		{
			this.type = type;
			this.playerName = playerName;
		}
	}
}