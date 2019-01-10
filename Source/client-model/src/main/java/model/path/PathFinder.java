package model.path;

import java.util.List;

import model.component.field.Field;


public interface PathFinder {

	List<Field> findPath(Field start, Field destination);

}
