
namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class AttackModelEvent : ModelEvent
	{

		public AttackType attackType;

		public Point2D sourceField;
		public Point2D destinationField;

		public AttackModelEvent(string name,
			AttackType attackType,
			Point2D sourceField,
			Point2D destinationField)
			: base(ModelEventType.AttackModelEvent, name)
		{

			this.attackType = attackType;

			this.sourceField = sourceField;
			this.destinationField = destinationField;
		}
	}
}