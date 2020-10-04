// Se Eun Jeh
// sjeh@usc.edu
// ITP 368 Fall 2020
// Basic GUI Paint program
package PaintJeh;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mousing.Point;

// fix: 
//output file with labels 
// functionalities to add


public class Paint extends Application {
	Stage stage;
	Pane root;

	// drawing space
	Pane drawing;

	// tool selection panel
	VBox tools;	
	Button[] buttons = new Button[6];
	Button rectangle, line, select, delete, load, save;
	TextField text;

	// mode to know which tool is being used
	String mode = "";

	// stores shapes that are made
	List<Shapes> shapes = new ArrayList<Shapes>();

	// stores currently selected object
	Shapes click = null;

	// for moving coordinates 
	Point[] corners = new Point[2];

	// global variables for shape selection
	RectangleR r;
	LineL l;
	Point p;
	Color color = Color.LIGHTSEAGREEN;
	boolean mouseIsPressed = false;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage)
	{
		// 6 lines
		stage.setTitle("Paint");
		root = new Pane();
		Scene scene = new Scene(root, 600, 600);
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(e->{Platform.exit(); System.exit(0);});
		
		// css file
//		File f = new File("paintstyle.css");
//		scene.getStylesheets().clear();
//		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

		// set up pane
		setUp();

		// run program
		runner();
	}

	public void setUp() {
		tools = new VBox();
		drawing = new Pane();
		Label header = new Label("paint program");
		tools.getChildren().add(header);

		// TOOLPANE
		buttons[0] = new Button("rectangle");
		
//		buttons[0].setStyle("-fx-background-image: "
//				+ "-fx-background-size: cover");
		
		buttons[1] = new Button("line");
		buttons[2] = new Button("select");
		buttons[3] = new Button("delete");
		buttons[4] = new Button("load");
		buttons[5] = new Button("save");

		// TOOL BUTTONS
		for (int i = 0; i < 6; i++) {
			System.out.println(buttons[i].getMinWidth());
			buttons[i].setMinSize(70, 20);
			
			int j = i; 
			if (buttons[j].getText().compareTo("save") == 0) {
				// if save button is clicked, run doSave()
				buttons[j].setOnAction((ActionEvent e)-> {
					mode = buttons[j].getText(); // set mode to button's name
					doSave(stage);
					System.out.println("mode: " + mode);
				});
			}
			else if (buttons[j].getText().compareTo("load") == 0) {
				// if load button is clicked, run doLoad()
				buttons[j].setOnAction((ActionEvent e)-> {
					mode = buttons[j].getText(); // set mode to button's name
					doLoad(stage);
					System.out.println("mode: " + mode);
				});	
			}
			else {
				buttons[j].setOnAction((ActionEvent e)-> {
					mode = buttons[j].getText(); // set mode to button's name
					System.out.println("mode: " + mode);
				});
			}
			tools.getChildren().add(buttons[i]);
		}
		
		// TEXT
		Text box = new Text();
		root.getChildren().add(box);
		box.relocate(100, 100);
		text = new TextField();
		text.setMaxWidth(70);
		text.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("text changed from " + oldValue + " to " + newValue);
			// add text to rectangle
			box.setText(newValue);
			if (click!=null) click.label(newValue);
		});
		Label textLabel = new Label("text:");
		textLabel.setLabelFor(text); 
		tools.getChildren().add(textLabel);
		tools.getChildren().add(text);

		// COLOR
		ColorPicker colorPicker = new ColorPicker(Color.LIGHTSEAGREEN);

		// picking a new color
		colorPicker.setOnAction((ActionEvent e)-> {
			color = (colorPicker.getValue());
			// if object is selected, make object the selected color
			if (click!=null) { 
				click.setStroke(color);
				click.setFill(color);
			}
		});
		tools.getChildren().add(colorPicker);
		root.getChildren().add(tools);
		root.getChildren().add(drawing);
	} 

	public void runner() {
		// mouse pressed
		root.addEventHandler
		(MouseEvent.MOUSE_PRESSED, (MouseEvent m) -> {
			p = new Point(m);
			mouseIsPressed = true;
			corners[0] = p;
			if (mode.compareTo("rectangle") == 0) { // mode rectangle
				r = new RectangleR(p, root, color); // create rectangle
				shapes.add(r); // add to shapes array
				click = r; // mark as currently selected object
			}
			else if (mode.compareTo("line") == 0) { // mode line
				l = new LineL(p, root, color);
				shapes.add(l);
				click = l; // mark as currently selected object
			}
			else if (mode.compareTo("select") == 0) { // mode select	
				click = findShape(p); // find object that is clicked
				if (click != null) {
					System.out.println("object has been found");
					click.setStroke(Color.RED); // feedback for selected shape 
					click.setStrokeWidth(2);
				}
			}
			else if (mode.compareTo("delete") == 0) { // mode delete
				click = findShape( p );
				System.out.println(click);
				shapes.remove(click); // remove selected object from list
				if (click!=null) click.remove(root);// remove selected object from drawing
			}
		});
		
		// mouse released 
		root.addEventHandler
		(MouseEvent.MOUSE_RELEASED, (MouseEvent m) -> {
			mouseIsPressed = false;
			if (mode.compareTo("select") == 0) { // mode select
				// reverse feedback for selected object
				if (click!=null) {
					click.setStroke(color);
					click.setStrokeWidth(1);
				}
			}
		});
		
		// mouse dragged 
		root.addEventHandler
		(MouseEvent.MOUSE_DRAGGED, (MouseEvent m) -> {   
			p = new Point(m);
			// make a rectangle with drag 
			if (mode.compareTo("rectangle") == 0) { // mode rectangle
				click.draw(corners[0],p); // finish drawing rectangle
			}
			// make a line with drag 
			else if (mode.compareTo("line") == 0) { // mode line
				click.draw(p, corners[0]); // finish drawing line
			}
			// dragging while select: moving the shape
			else if (mode.compareTo("select") == 0) { // mode select
				if (click != null) {
					click.dragged(p); // move selected object
				}
			}
		});
	}

	// finds shape from our list
	public Shapes findShape(Point p) 
	{
		// iterate through shapes list
		for (Shapes e : shapes){
			if (e.zatYou(p)) { 
				// if shape was found, return it
				return e;
			}
		}
		return null;
	}


	//filechoosercode
	public void doSave(Stage stage)
	{
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory( new File(".") );
		fc.setTitle("choose file to save");
		File fi = fc.showSaveDialog(stage);
		try
		{
			FileWriter outy = new FileWriter(fi);
			// iterate through shapes list 
			for ( Shapes t : shapes )
			{
				// write each shape to file
				outy.write(t.text());	
			}
			outy.close();
		}
		catch(Exception e3) { System.out.println("file write error"); }
	}

	public void doLoad(Stage stage)
	{
		System.out.println("LOAD WORKS FOR LOADING OBJECTS, BUT NOT TEXT FYI");
		System.out.println("PLEASE TEST WITH FILE WITH JUST SHAPES... thanks");
		
		// clear existing drawing
		root.getChildren().clear();
		// set up pane and runner again
		setUp();
		runner();
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory( new File(".") );
		fc.setTitle("choose file to load");
		File fi = fc.showOpenDialog(stage);
		try
		{
			Scanner scan = new Scanner(fi);
			// while file is not empty
			while ( scan.hasNext() )
			{
				// read in fields of objects
				String shape = scan.next();
				Double x = scan.nextDouble();
				Double y = scan.nextDouble();
				Double x1 = 0.0;
				Double y1 = 0.0;
				String fill = "";

				String text = "";
				if (scan.hasNextDouble()) // not a word 
				{
					System.out.println("not Word");
					x1 = scan.nextDouble();
					y1 = scan.nextDouble();
				}
				else { // a word
					
					text = scan.next();
					System.out.println("Word: " + text );
				}
				if (scan.hasNext()) fill =  scan.next();

				if (shape.compareTo("rect")==0) { // shape read was rectangle
					// create line with specified parameters from file
					Rectangle r = new Rectangle(x,y,x1,y1);
					System.out.println("color val: " + fill);
					r.setFill(Color.valueOf(fill));
					r.setStroke(Color.valueOf(fill));
					root.getChildren().add(r);
				}
				else if (shape.compareTo("line")==0) { // shape read was line
					// create line with specified parameters from file
					Line l = new Line(x,y,x1,y1);
					Color c = Color.valueOf(fill);
					if (c == null) c = Color.BLACK;
					l.setFill(Color.valueOf(fill));
					l.setStroke(Color.valueOf(fill)); 
					root.getChildren().add(l);
				}
				else if (shape.compareTo("word")==0) { // shape read was word
					// create label with specified parameters from file
					Label label = new Label(text);
//					label.setLayoutX(x);
//					
//					label.setLayoutX(y);
					label.relocate(x, y);
					label.setTextFill(Color.BLACK);
					root.getChildren().add(label);
				}
			}
			scan.close();
		}
		catch(Exception e2 ) { 
			e2.printStackTrace();
			System.out.println("file read error"); 
		}
	}
}
