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
import root.view.menu.DescriptionItem;
import view.ResourceManager;

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
		if (model.getMove() != null) {
			moveType = model.getMove().getName();
			moveSpeed = model.getMove().getDelay();
		}

		attacks = model.getAttacks().stream()
				.map(AttackInfo::fromModel)
				.collect(Collectors.toList());

		if (model.getActiveAttack() != null) {
			activeAttack = model.getActiveAttack().type;

			var target = model.getActiveAttack().getTarget();

			opponentName = target.getPlayer().getUsername();
			opponentHealth = target.getUnit().getHealth();
			if (target.getUnit().getDefense() != null) {
				opponentAttack = AttackInfo.fromModel(target.getUnit().getDefense());
			}
		}

		if (model.getDefense() != null) {
			defense = model.getDefense().type;
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

	public DescriptionItem describeUnit() {
		return new DescriptionItem("Unit",
				Arrays.asList("Name: " + unitName,
						"Health: " + health,
						"MoveType: " + (moveType != null ? moveType : "None"),
						"MoveSpeed: " + (moveType != null ? moveSpeed : 0)),
				ResourceManager.getInstance().constructUnitKey(unitName));
	}

	public List<DescriptionItem> describeAttacks() {
		if (attacks == null) {
			return Arrays.asList();
		}

		return attacks.stream()
				.map(AttackInfo::describe)
				.collect(Collectors.toList());

	}

	public DescriptionItem describeActiveAttack() {
		if (!isAttacking()) {
			return null;
		}

		return new DescriptionItem("Active",
				Arrays.asList(
						"Attacking: " + opponentName,
						"With: " + activeAttack,
						"OpponentHealth: " + opponentHealth,
						"OpponentDefense: " + (opponentAttack != null ? opponentAttack.name : "None"),
						"\t dmg: " + (opponentAttack != null ? opponentAttack.defenseDmg : "0"),
						"\t cooldown: " + (opponentAttack != null ? opponentAttack.defenseCooldown : "0"),
						"\t range: " + (opponentAttack != null ? opponentAttack.defenseRange : "0")),
				null);
	}

	public DescriptionItem describeDefense() {
		if (defense == null) {
			return null;
		} else {
			if (isDefending()) {
				// If activeAttack is not null but the opponents data is set it means 
				// that  we are under the attack.
				return new DescriptionItem("Defense",
						Arrays.asList(
								"Defending from: " + opponentName,
								"With: " + defense,
								"OpponentHealth: " + opponentHealth,
								"OpponentAttack: " + (opponentAttack != null ? opponentAttack.name : "None"),
								"\t dmg: " + (opponentAttack != null ? opponentAttack.attackDmg : "0"),
								"\t cooldown: " + (opponentAttack != null ? opponentAttack.attackCooldown : "0"),
								"\t range: " + (opponentAttack != null ? opponentAttack.attackRange : "0")),
						null);
			} else {
				// We have prepared defense but nobody is attacking us. 
				return new DescriptionItem("Defense", Arrays.asList(defense), null);
			}
		}

	}

	public boolean isAttacking() {
		return this.activeAttack != null;
	}

	public boolean isDefending() {
		return this.activeAttack == null && this.opponentName != null;
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

		public static DescriptionItem describe(AttackInfo att) {
			return new DescriptionItem(att.name,
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
