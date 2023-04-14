
namespace RabbitGameServer.SharedModel.ClientIntentions
{
	public class AttackIntention : ClientIntention
	{

		public AttackType attackType;

		public Point2D sourceField;
		public Point2D destinationField;

		public AttackIntention(string name,
			AttackType attackType,
			Point2D sourceField,
			Point2D destinationField)
			: base(ClientIntentionType.Attack, name)
		{

			this.attackType = attackType;

			this.sourceField = sourceField;
			this.destinationField = destinationField;
		}
	}
}