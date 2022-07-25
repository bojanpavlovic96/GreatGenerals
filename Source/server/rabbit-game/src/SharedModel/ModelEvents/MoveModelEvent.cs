namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class MoveModelEvent : ModelEvent
	{

		public Point2D? sourceField;
		public Point2D? destinationField;

	}
}