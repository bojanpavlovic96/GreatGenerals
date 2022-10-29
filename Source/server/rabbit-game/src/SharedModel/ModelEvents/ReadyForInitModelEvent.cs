
namespace RabbitGameServer.SharedModel.ModelEvents
{

	public class ReadyForInitModelEvent : ModelEvent
	{

		public String roomName{get;set;}

		public ReadyForInitModelEvent(string playerName, String roomName)
			: base(ModelEventType.ReadyForInitEvent, playerName)
		{
			this.roomName = roomName;
		}
	}
}