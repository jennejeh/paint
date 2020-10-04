// Se Eun Jeh
// sjeh@usc.edu
// ITP 368 Fall 2020
package PaintJeh;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import mousing.Point;

public class LineL extends Shapes{
	Line l;
	
	// constructor starts drawing line
	public LineL(Point p, Pane pane,Color color) {
		l = new Line();
		l.setFill(color);
		l.setStroke(color);
		l.setStartX(p.getX());
		l.setStartY(p.getY());
		pane.getChildren().add(l);
	}
	
	// zatYou for line
	public boolean zatYou( Point m )
	{
		System.out.println("hi pls select near the top or bottom of the line");
		double d = Math.abs(m.getX()-l.getStartX())+Math.abs(m.getY()-l.getStartY());
		double c = Math.abs(m.getX()-l.getEndX())+Math.abs(m.getY()-l.getEndY());
		boolean ret = (d<50) || (c<50);
		System.out.println("Line found? " + ret);
		return ret;
	}
	
	// converts line & its parameters to text for save & load
	public String text() {
		String str = "line " + l.getStartX() + " " +  l.getStartY() + " " + l.getEndX() + " " + l.getEndY() + " " + l.getFill() + "\n";
		return str;
	}
	
	// removes line from pane
	public void remove(Pane pane) {
		System.out.println("removing line");
		pane.getChildren().remove(l);
	}

	// draws line
	public void draw(Point p, Point q)
	{
		System.out.println("drawing line");
		l.setEndX(p.getX());
		l.setEndY(p.getY());
	}

	// moves line to the dragged point
	public void dragged(Point p) 
	{
		System.out.println("dragging line");
		l.relocate(p.getX(), p.getY());
	}
	
	// empty text function for line
	public void label(String text) {}
	
	// setters for line
	public void setStroke(Color c) {
		l.setStroke(c);	
	}
	public void setFill(Color c) {
		l.setFill(c);	
	}
	public void setStrokeWidth(double value)
	{
		l.setStrokeWidth(value);
	}


}
