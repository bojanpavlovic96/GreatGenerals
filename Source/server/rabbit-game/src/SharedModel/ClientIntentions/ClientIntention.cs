namespace RabbitGameServer.SharedModel.ClientIntentions
{

	public enum ClientIntentionType
	{
		Move,
		Attack,
		Defend,
		ReadyForInit,
		AbortAttack,
		BuildUnit,
		ReadyForReplay,
		LeaveGame
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