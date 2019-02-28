package model.component.unit;

import java.util.ArrayList;
import java.util.List;

import model.component.field.Field;

public class UnitCreator {

	private List<Unit> prototypes;

	public UnitCreator() {

		this.prototypes = new ArrayList<Unit>();

	}

	public Unit generateUnit(String unit_name, Field new_position,
			MoveEventHandler move_handler /* attack handler and arit attack handler */) {
		for (Unit prototype : this.prototypes) {

			if (prototype.getUnitName().equals(unit_name)) {
				// prototype with given name exists

				try {

					Unit clone = prototype.clone();
					clone.getMoveType().setMyField(new_position);

					if (clone.canMove()) {
						clone.getMoveType().setOnMoveHandler(move_handler);
					}

					/*
					 * if clone.can attack set attack handler
					 * 
					 * if clone.can air attack set air attack handler
					 * 
					 */

					return clone;

				} catch (CloneNotSupportedException e) {
					System.out.println("Exception while cloning unit ... @ UnitCreator.generateUnit");
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
