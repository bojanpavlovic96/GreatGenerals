package root.communication.messages.components;

public class AttackDesc {
	// enum on the server side
	public String type;

	public int damage;

	public int range;
	public long duration;
	public long cooldown;

	public AttackDesc() {

	}
}
