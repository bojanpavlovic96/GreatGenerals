package protocol;

import root.communication.messages.MessageType;

public interface NameTypeResolver {
	Class<?> resolve(MessageType type);
}
