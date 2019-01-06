package model.component.unit;

import java.util.ArrayList;
import java.util.List;

import model.component.Field;

public class UnitCreator {

	private List<Unit> prototypes;

	public UnitCreator() {

		this.prototypes = new ArrayList<Unit>();

	}

	public Unit generateUnit(String unit_name, Field new_position) {
		for (Unit prototype : this.prototypes) {
			if (prototype.getUnitName().equals(unit_name)) {
				try {
					prototype.getMoveType().setMy_field(new_position);
					return prototype.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void addPrototype(Unit unit_prototype) {

		this.prototypes.add(unit_prototype);

	}

}
