package root.model;

import java.util.List;

import root.Point2D;
import root.communication.PlayerDescription;
import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.FieldDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.UnitDesc;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.UnitType;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventProducer;

public interface Model extends ModelEventProducer{

	List<Field> getFields();

	List<Field> getFreeNeighbours(Field currentField, int range);

	Field getField(Point2D storagePosition);

	void setField(Field field);

	void initializeModel(List<PlayerDescription> players,
			List<UnitDesc> units,
			List<MoveDesc> moves,
			List<AttackDesc> attacks,
			List<FieldDesc> fields,
			List<FieldOption> options);

	Unit generateUnit(UnitType type, PlayerData owner);

	boolean destroyUnit(Unit unit);

	int distance(Field aField, Field bField);

	List<Unit> getActiveUnits();

	// the fact that UnitDesc is from communication package is bothering me 
	List<UnitDesc> getUnits();

	void removeUnit(Unit unit);

	PlayerData getPlayer(String name);

	PlayerData getOwner();
}
