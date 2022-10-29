namespace RabbitGameServer.SharedModel
{

	// TODO does not look like the best approach, actually it is kinda useless ... 
	// I am wrapping a message, which has getName method, in an wrapper just so it 
	// can have a name ... 
	// This way incoming byte stream  has to be serialized to a wrapper class and then 
	// payload has to be  serialized to an actual message according to the wrapper's name field 
	// maybe better approach: serialize message as it is and then deserialize it to the 
	// message (base class) then get a name and according to it deserialize the same byte payload 
	// (again) to the actual concrete type.
	// This would of course be the alg. for serializers that do not support polymorphism

	// The whole point of wrapper was to kinda be able to send anything trough payload 
	// as long as it can be named ... but ... should I actually send random stuff ... 
	// If only objs. of message class can be sent ... that is pretty much well defined 
	// protocol ... no ? 

	public class NamedWrapper
	{

		public string name;
		public string payload;

		public NamedWrapper(String name, String payload)
		{
			this.name = name;
			this.payload = payload;
		}

	}
}