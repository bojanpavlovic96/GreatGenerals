
namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class DefendModelEvent : ModelEvent
	{

		public AttackType defenseType;

		public Point2D sourceField;
		public Point2D destinationField;

		public DefendModelEvent(string name,
			AttackType defenseType,
			Point2D sourceField,
			Point2D destinationField)
			: base(ModelEventType.DefendModelEvent, name)
		{

			this.defenseType = defenseType;

			this.sourceField = sourceField;
			this.destinationField = destinationField;
		}
	}
}