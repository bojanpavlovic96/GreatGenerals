package app.launcher;

import controller.command.CtrlInitializeCommand;
import controller.command.CtrlMoveCommand;
import protocol.NameTypeResolver;
import root.command.Command;

public class StupidStaticTypeResolver implements NameTypeResolver {

	@Override
	public Class<?> resolve(String name) {

		switch (name) {

		case CtrlInitializeCommand.name:
			return CtrlInitializeCommand.class;

		case CtrlMoveCommand.name:
			return CtrlMoveCommand.class;

		}

		return Command.class;
	}

}
