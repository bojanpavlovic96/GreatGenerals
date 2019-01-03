package view.component;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public interface HexCoordinateConverter {

	Point3D convertToCube(Point2D axial);

	Point2D convertToAxial(Point3D cube);

	Point2D calcRealPosition(Point2D axial);

	Point2D calcStoragePosition(Point2D point);

}
