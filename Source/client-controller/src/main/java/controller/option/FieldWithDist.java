package controller.option;

import root.model.component.Field;

public class FieldWithDist implements Comparable<FieldWithDist> {
	public int distance;
	public Field field;

	public FieldWithDist(Field field, int distance) {
		this.distance = distance;
		this.field = field;
	}

	@Override
	public int compareTo(FieldWithDist arg0) {
		return ((Integer) distance).compareTo(arg0.distance);
	}

}
