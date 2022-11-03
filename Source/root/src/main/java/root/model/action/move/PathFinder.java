package root.model.action.move;

import java.util.List;

import root.model.Model;
import root.model.component.Field;

public interface PathFinder {

	List<Field> findPath(Model dataModel, Field start, Field destination);

}
