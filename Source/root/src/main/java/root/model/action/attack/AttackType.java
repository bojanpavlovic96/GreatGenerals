package root.model.action.attack;

public class AttackType implements Cloneable {

	public int hitDamage;
	public int range;
	public int speed;

	// description maybe ... 

	public AttackType() {
	}

	@Override
	public AttackType clone() {
		AttackType clone = new AttackType();
		clone.hitDamage = this.hitDamage;
		clone.range = this.range;
		clone.speed = this.speed;
		return clone;
	}

}
