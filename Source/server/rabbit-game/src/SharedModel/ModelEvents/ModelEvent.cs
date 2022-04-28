namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class ModelEvent
	{
		public string eventName { get; set; }
		public string playerName { get; set; }
		// TODO move room name to modelEventRequest 
		// I guess it should be extracted from routing key or something 
		// because original ModelEventArg (clinet java version) does not have that
		// field, but it could be added, this way ModelEventArg would get translated
		// to the class defined in 'communication protocol' which should be part of
		// the shared model 
		public string roomName { get; set; }

	}
}