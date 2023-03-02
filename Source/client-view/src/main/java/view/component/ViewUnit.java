package view.component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import root.Point2D;
import root.model.action.attack.Attack;
import root.model.component.Unit;
import root.view.Color;
import view.ResourceManager;
import view.component.menu.DescMenuItem;

public class ViewUnit {

	private Color highlightColor;

	private String unitName;

	private int health;

	private String moveType;
	private float moveSpeed;

	private List<AttackInfo> attacks;

	private String activeAttack;

	private String defense;

	private String opponentName;
	private int opponentHealth;
	private AttackInfo opponentAttack;

	public ViewUnit(Unit model) {
		unitName = model.getUnitType().toString();

		health = model.getHealth();
		moveType = model.getMove().getName();
		moveSpeed = model.getMove().getDelay();

		attacks = model.getAttacks().stream()
				.map(AttackInfo::fromModel)
				.collect(Collectors.toList());

		if (model.getActiveAttack() != null) {
			activeAttack = model.getActiveAttack().type;

			var target = model.getActiveAttack().getTarget();

			opponentName = target.getPlayer().getUsername();
			opponentHealth = target.getUnit().getHealth();
			opponentAttack = AttackInfo.fromModel(target.getUnit().getDefense());
		}

		defense = model.getDefense().type;

		if (model.isDefending()) {

		}

		var pColor = model.getOwner().getColor();
		this.highlightColor = new Color(pColor.red,
				pColor.green,
				pColor.blue,
				0.3); // TODO this opacity should be in config

	}

	public void drawUnit(GraphicsContext gc, Point2D hexCenter, double hexSide) {

		Image image = ResourceManager.getInstance().getUnit(this.unitName);

		gc.save();

		double image_width = hexSide * 1.4;
		double image_height = hexSide * 1.4;

		gc.drawImage(image,
				hexCenter.getX() - image_width / 2,
				hexCenter.getY() - image_height / 2,
				image_width,
				image_height);
		gc.restore();

	}

	public DescMenuItem describeUnit() {
		return new DescMenuItem("Unit",
				Arrays.asList("Name: " + unitName,
						"Health: " + health,
						"MoveType: " + moveType,
						"MoveSpeed: " + moveSpeed),
				ResourceManager.getInstance().constructUnitKey(unitName));
	}

	public List<DescMenuItem> describeAttacks() {
		if (attacks == null) {
			return Arrays.asList();
		}

		return attacks.stream()
				.map(AttackInfo::describe)
				.collect(Collectors.toList());

	}

	public DescMenuItem describeActiveAttack() {
		if (activeAttack == null) {
			return null;
		}

		return new DescMenuItem("Active",
				Arrays.asList(
						"Attacking: " + opponentName,
						"With: " + activeAttack,
						"OpponentHealth: " + opponentHealth,
						"OpponentDefense: " + opponentAttack.name,
						"\t dmg: " + opponentAttack.defenseDmg,
						"\t cooldown: " + opponentAttack.defenseCooldown,
						"\t range: " + opponentAttack.defenseRange),
				null);
	}

	public DescMenuItem describeDefense() {
		if (defense == null) {
			return null;
		} else if (opponentName != null) {
			// If defense is not null and opponents data is set it means that 
			// we are under the attack.
			return new DescMenuItem("Defense",
					Arrays.asList(
							"Defending from: " + opponentName,
							"With: " + defense,
							"OpponentHealth: " + opponentHealth,
							"OpponentAttack: " + opponentAttack.name,
							"\t dmg: " + opponentAttack.attackDmg,
							"\t cooldown: " + opponentAttack.attackCooldown,
							"\t range: " + opponentAttack.attackRange),
					null);
		} else {
			// We have prepared defense but nobody is attacking us. 
			return new DescMenuItem("Defense", Arrays.asList(defense), null);
		}

	}

	public Color getHighlightColor() {
		return this.highlightColor;
	}

	private static class AttackInfo {
		public String name;

		public int attackDmg;
		public long attackCooldown;
		public int attackRange;

		public int defenseDmg;
		public long defenseCooldown;
		public int defenseRange;

		public AttackInfo(String name,
				int attackDmg,
				long attackCooldown,
				int attackRange,
				int defenseDmg,
				long defenseCooldown,
				int defenseRange) {

			this.name = name;
			this.attackDmg = attackDmg;
			this.attackCooldown = attackCooldown;
			this.attackRange = attackRange;
			this.defenseDmg = defenseDmg;
			this.defenseCooldown = defenseCooldown;
			this.defenseRange = defenseRange;
		}

		public static AttackInfo fromModel(Attack att) {
			return new AttackInfo(att.type,
					att.attackDmg,
					att.attackCooldown,
					att.attackRange,
					att.defenseDmg,
					att.defenseCooldown,
					att.defenseRange);
		}

		public static DescMenuItem describe(AttackInfo att) {
			return new DescMenuItem(att.name,
					Arrays.asList("Attack Damage: " + att.attackDmg,
							"Attack Cooldown: " + att.attackCooldown,
							"Attack Range: " + att.attackRange,
							"Defense Damage: " + att.defenseDmg,
							"Defense Cool: " + att.defenseCooldown,
							"Defense Range" + att.defenseRange),
					null);
		}

	}

}
