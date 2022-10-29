
namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class AttackModelEvent : ModelEvent
	{
		public Point2D? sourceField;
		public Point2D? destinationField;

		public AttackModelEvent(string name, Point2D? sourceField, Point2D? destinationField)
			: base(ModelEventType.AttackModelEvent, name)
		{
			this.sourceField = sourceField;
			this.destinationField = destinationField;
		}
	}
}