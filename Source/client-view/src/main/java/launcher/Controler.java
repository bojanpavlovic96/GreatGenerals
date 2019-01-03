package launcher;

public class Controler {

	private DebugStage stage;

	public Controler(DebugStage stage) {

		this.stage = stage;

	}

	public void addCricle() {

		System.out.println("Passing circle command ...");
		this.stage.addCircle(new CircleAdder());

	}
}
