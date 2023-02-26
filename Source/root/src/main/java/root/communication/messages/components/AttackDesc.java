package root.communication.messages.components;

import root.communication.parser.StaticParser;

public class AttackDesc {

	// enum on the server side
	public String type;

	public int attackDmg;
	public long attackCooldown;
	public int attackRange;

	public int defenseDmg;
	public long defenseCooldown;
	public int defenseRange;

	public long duration;

	public AttackDesc() {

	}

	@Override
	public String toString() {
		return StaticParser.ToString(this);
	}

}
