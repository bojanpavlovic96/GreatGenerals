package app.launcher;

import protocol.NameTypeResolver;
import root.command.Command;
import root.communication.messages.InitializeCmdMsg;
import root.communication.messages.MoveCmdMsg;

public class StupidStaticTypeResolver implements NameTypeResolver {

	@Override
	public Class<?> resolve(String name) {

		switch (name) {

			case InitializeCmdMsg.name:
				return InitializeCmdMsg.class;

			case MoveCmdMsg.name:
				return MoveCmdMsg.class;

		}

		return Command.class;
	}

}
