package root.model;

import java.util.List;

import javafx.geometry.Point2D;
import root.ActiveComponent;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.option.FieldOption;
import root.model.component.option.UnitOption;
import root.model.event.ModelEventHandler;

public interface Model extends ActiveComponent {

	List<Field> getFields();

	List<Field> getFreeNeighbours(Field curren_field);

	Field getField(Point2D storage_position);

	void setField(Field field);

	void fillModel(List<PlayerData> list_of_players, List<Field> fields);

	void initializeModel(ModelEventHandler eventHandler, List<FieldOption> fieldOptions,
			List<UnitOption> unitOptions);

	List<FieldOption> getFieldOptions();

	List<UnitOption> getUnitOptions();

	// TODO maybe add some interface for changing field/unit options

	Unit generateUnit(String unit_name);

}
