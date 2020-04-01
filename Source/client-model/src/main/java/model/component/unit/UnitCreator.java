package model.component.unit;

import java.util.HashMap;
import java.util.Map;

import root.model.component.Unit;

public class UnitCreator {

	private Map<String, Unit> prototypes;

	public UnitCreator() {

		this.prototypes = new HashMap<String, Unit>();

	}

	public Unit generateUnit(String unit_name) {

		Unit prototype = this.prototypes.get(unit_name);

		if (prototype != null) {

			try {

				Unit clone = prototype.clone();

				return clone;

			} catch (CloneNotSupportedException e) {
				System.out.println("Exception while cloning unit ... @ UnitCreator.generateUnit");
				e.printStackTrace();
			}

		}

		// if exception occur
		return null;
	}

	public void addPrototype(Unit unit_prototype) {

		this.prototypes.put(unit_prototype.getUnitName(), unit_prototype);

	}

}
