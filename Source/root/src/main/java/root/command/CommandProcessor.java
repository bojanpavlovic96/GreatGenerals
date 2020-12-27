package root.command;

// TODO make abstraction over fx.PlatformRunLater (using custom interface)
// and to the same with JavaThreadPool executor
// so then they both can be injected as dependencies inside constructor
public interface CommandProcessor {

	void execute(CommandQueue commandQueue);

}
