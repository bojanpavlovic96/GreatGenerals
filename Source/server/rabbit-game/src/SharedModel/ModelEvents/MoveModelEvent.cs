namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class MoveModelEvent : ModelEvent
	{

		public Point2D sourceField;
		public Point2D destinationField;

		public MoveModelEvent(string playerName, Point2D sField, Point2D dField)
			: base(ModelEventType.MoveModelEvent, playerName)
		{

			this.sourceField = sField;
			this.destinationField = dField;
		}
	}
}