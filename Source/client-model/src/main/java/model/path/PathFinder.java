package model.path;

import java.util.List;

import model.component.Field;

public interface PathFinder {

	List<Field> findPath(Field start, Field destination);

}
