package model.component.unit;

import java.util.HashMap;
import java.util.Map;

import model.component.field.Field;

public class UnitCreator {

	private Map<String, Unit> prototypes;

	public UnitCreator() {

		this.prototypes = new HashMap<String, Unit>();

	}

	public Unit generateUnit(String unit_name) {

		Unit prototype = this.prototypes.get(unit_name);

		if (prototype != null) {
			// prototype with given name exists

			try {

				Unit clone = prototype.clone();

				return clone;

			} catch (CloneNotSupportedException e) {
				System.out.println("Exception while cloning unit ... @ UnitCreator.generateUnit");
				e.printStackTrace();
			}

		}

		return null;
	}

	public void addPrototype(Unit unit_prototype) {

		this.prototypes.put(unit_prototype.getUnitName(), unit_prototype);

	}

}
