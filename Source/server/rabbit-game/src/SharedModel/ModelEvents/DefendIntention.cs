
namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class DefendIntention : ClientIntention
	{

		public AttackType defenseType;

		public Point2D sourceField;
		public Point2D destinationField;

		public DefendIntention(string name,
			AttackType defenseType,
			Point2D sourceField,
			Point2D destinationField)
			: base(ClientIntentionType.Defend, name)
		{

			this.defenseType = defenseType;

			this.sourceField = sourceField;
			this.destinationField = destinationField;
		}
	}
}