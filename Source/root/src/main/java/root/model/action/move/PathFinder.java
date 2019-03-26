package root.model.action.move;

import java.util.List;

import root.model.component.Field;

public interface PathFinder {

	List<Field> findPath(Field start, Field destination);

}
