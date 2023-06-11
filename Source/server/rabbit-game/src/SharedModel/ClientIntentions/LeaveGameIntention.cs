
namespace RabbitGameServer.SharedModel.ClientIntentions
{
	public class LeaveGameIntention : ClientIntention
	{
		public LeaveGameIntention(string playerName)
			: base(ClientIntentionType.LeaveGame, playerName)
		{
		}
	}
}