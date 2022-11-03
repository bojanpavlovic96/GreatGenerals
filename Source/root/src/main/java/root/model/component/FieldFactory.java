package root.model.component;

import root.Point2D;
import root.model.PlayerData;

public interface FieldFactory {
	Field getField(boolean visibility,
			Point2D position,
			Unit unit,
			Terrain terrain,
			PlayerData owner);
}
