// Se Eun Jeh
// sjeh@usc.edu
// ITP 368 Fall 2020
package PaintJeh;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import mousing.Point;

public abstract class Shapes extends Object{
// abstract container class for rectangles and lines
	public abstract void setStroke(Color c);
	public abstract void setFill(Color c); 
	public abstract void setStrokeWidth(double value);
	public abstract void label(String text);
	public abstract void dragged(Point p);
	public abstract void draw(Point a, Point b);
	public abstract String text();
	public abstract void remove(Pane pane);
	public abstract boolean zatYou( Point m );
	
}
