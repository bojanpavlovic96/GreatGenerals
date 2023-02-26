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

	public ViewUnit(Unit model) {
		this.unitName = model.getUnitType().toString();

		health = model.getHealth();
		moveType = model.getMove().getName();
		moveSpeed = model.getMove().getDelay();

		attacks = model.getAttacks().stream()
				.map(AttackInfo::fromModel)
				.collect(Collectors.toList());

		if (model.getActiveAttack() != null) {
			activeAttack = model.getActiveAttack().type;
		}

		defense = model.getDefense().type;

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
				Arrays.asList("Name " + unitName,
						"Health  " + health,
						"MoveType " + moveType,
						"MoveSpeed " + moveSpeed),
				null);
	}

	public List<DescMenuItem> describieAttacks() {
		if (attacks == null) {
			return Arrays.asList();
		}

		return attacks.stream()
				.map(AttackInfo::describe)
				.collect(Collectors.toList());

	}

	public DescMenuItem describeActiveAttack() {
		if (activeAttack != null) {
			return null;
		}

		return new DescMenuItem("Active", Arrays.asList(activeAttack), null);
	}

	public DescMenuItem describeDefense() {
		if (defense != null) {
			return null;
		}

		return new DescMenuItem("Defense", Arrays.asList(defense), null);
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
					Arrays.asList("attDmg: " + att.attackDmg,
							"attCool: " + att.attackCooldown,
							"attRange: " + att.attackRange,
							"defnseDmg: " + att.defenseDmg,
							"defCool: " + att.defenseCooldown,
							"defRange" + att.defenseRange),
					null);
		}

		// public ViewFieldDescription.ViewAttackDesc describe() {
		// 	return new ViewFieldDescription.ViewAttackDesc(
		// 			name,
		// 			Arrays.asList(
		// 					"attDmg: " + attackDmg,
		// 					"attCool: " + attackCooldown,
		// 					"attackRange: " + attackRange,
		// 					"defDmg :" + defenseDmg,
		// 					"defCool: " + defenseCooldown,
		// 					"defRange: " + defenseRange),
		// 			null);
		// }

	}

}
