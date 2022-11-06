package protocol;

// Does not look like the best approach, actually it is kinda useless ... 
// I am wrapping a message, which has getType method, in an wrapper just so it 
// can have a name ... 
// this way incoming byte stream  has to be serialized to a wrapper class and then 
// payload has to be  serialized to an actual message according to the wrapper's name field 
// maybe better approach: serialize message as it is and then deserialize it to the 
// message (base class) then get a name and according to it deserialize the same byte payload 
// (again) to the actual concrete type

// this would of course be the alg. for serializer that do not support polymorphism
// EDIT: ^ every serializer that "supports polymorphism" is actually just adding 
// one more field to name the payload and then use that field for deserialization ... 

// the whole point of wrapper was to kinda be able to send anything trough payload 
// as long as it can be named ... but ... should I actually send random stuff ... 
// if only objs. of message class can be sent ... that is pretty much well defined 
// protocol ... no ? 

public class NamedWrapper {

	public String name;
	public String payload;

	public NamedWrapper() {
	}

	public NamedWrapper(String name, String payload) {
		this.name = name;
		this.payload = payload;
	}

}
