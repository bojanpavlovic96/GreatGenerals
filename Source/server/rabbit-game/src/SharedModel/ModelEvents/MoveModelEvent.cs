namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class MoveModelEvent : ModelEvent
	{

		public static string name = "move-model-event";

		public Point2D sourceField;
		public Point2D destinationField;

	}
}