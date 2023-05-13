
namespace RabbitGameServer.SharedModel.ClientIntentions
{
	public class ReadyForReplayIntention : ClientIntention
	{
		public string roomId;

		public ReadyForReplayIntention(string username, string roomId)
			: base(ClientIntentionType.ReadyForReplay, username)
		{
			this.roomId = roomId;
		}
	}
}