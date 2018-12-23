package board;

import figures.Figure;
import ui.Printable;

public class SimpleField implements Field,Printable{
	
	private Point position;
	
	private Figure figure;
	
	public SimpleField(Point position, Figure figure) {
		this.position = position;
		this.figure = figure;
	}

	public Point getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	public Figure getFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	public Field removeFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	public Field setFigure(Figure new_figure) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsFigure() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
