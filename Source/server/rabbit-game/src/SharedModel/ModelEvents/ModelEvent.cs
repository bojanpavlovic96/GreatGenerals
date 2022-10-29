namespace RabbitGameServer.SharedModel.ModelEvents
{

	public enum ModelEventType
	{
		MoveModelEvent,
		AttackModelEvent,
		ReadyForInitEvent
	}

	public class ModelEvent
	{

		public ModelEventType type { get; set; }
		public string playerName { get; set; }

		public ModelEvent(ModelEventType type, string playerName)
		{
			this.type = type;
			this.playerName = playerName;
		}
	}
}