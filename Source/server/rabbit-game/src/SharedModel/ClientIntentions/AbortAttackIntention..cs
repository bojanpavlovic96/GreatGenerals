
namespace RabbitGameServer.SharedModel.ClientIntentions
{
	public class AbortAttackIntention : ClientIntention
	{
		public Point2D position;

		public AbortAttackIntention(string playerName, Point2D position)
			: base(ClientIntentionType.AbortAttack, playerName)
		{
			this.position = position;
		}
	}
}