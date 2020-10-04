// Se Eun Jeh
// sjeh@usc.edu
// ITP 368 Fall 2020
package PaintJeh;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mousing.Point;

public class RectangleR extends Shapes{
	// makes a rectangle
	Rectangle r;
	Point x;
	Label l;
	StackPane s;
	double x1, y1;
	Pane root;

	// constructor starts drawing rectangle
	public RectangleR(Point p, Pane pane, Color color) {
		x1 = p.getX();
		y1 = p.getY();
		root = pane;
		r = new Rectangle(5,5);
		r.setFill(color);
		r.setStroke(color);
		r.setX(p.getX());
		r.setY(p.getY());
		pane.getChildren().add(r);
	}

	// removes rectangle from pane
	public void remove(Pane pane) {
		System.out.println("removing rectangle");
		pane.getChildren().remove(s);
	}

	// set text to rectangle
	public void label(String text) {
		l = new Label(text);
		l.setTextFill(Color.BLACK);
		root.getChildren().add(l);
		s = new StackPane();
		s.setLayoutX(x1);
		s.setLayoutY(y1);
		s.getChildren().add(r);
		s.getChildren().add(l);
		root.getChildren().add(s);
		
	}

	// zatYou for rectangle
	public boolean zatYou( Point m )
	{
		boolean ret = true;
		ret = ret && r.getX() < m.getX();
		ret = ret && r.getY() < m.getY();
		ret = ret && m.getX() < r.getX() + r.getWidth();
		ret = ret && m.getY() < r.getY() + r.getHeight();
		System.out.println("Rectangle found? " + ret);
		return ret;
	}

	// moves rectangle to the dragged point
	public void dragged(Point p)
	{
		System.out.println("dragging rectangle");
		if (s == null) r.relocate(p.getX(), p.getY());
		else s.relocate(p.getX(), p.getY());
	}

	// draws rectangle
	public void draw(Point a, Point b)
	{
		System.out.println("drawing rectangle");
		double xdif = a.xdif(b);
		double ydif = a.ydif(b);
		r.setWidth(xdif);
		r.setHeight(ydif);
		r.setX(min( a.getX(), b.getX()));
		r.setY(min( a.getY(), b.getY()));	
	}
	public double min( double x, double y)
	{
		return (x<y)? x : y;
	}

	// converts rectangle & its parameters to text for save & load
	public String text() {
		String str = "rect " + r.getX() + " " +  r.getY() + " " + r.getWidth() + " " + r.getHeight() + " " + r.getFill() + "\n"; 
		if (l!=null) {
			String word = "word " + l.getLayoutX() + " " + l.getLayoutY() + " " + l.getText() + " " + l.getTextFill() + "\n";
			StringBuffer x = new StringBuffer(str);
			x.append(word);
			str = x.toString();
		}
		return str;
	}

	// setters for rectangle
	public void setStroke(Color c) {
		r.setStroke(c);	
	}
	public void setFill(Color c) {
		r.setFill(c);	
	}
	public void setStrokeWidth(double value)
	{
		r.setStrokeWidth(value);
	}

}
