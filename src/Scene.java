package src;

import src.shapes.*;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * A Scene object contains the camera, screen, and all of the shapes and lights that are being rendered in the scene.
 *
 * It also stores the movements and rotations of all the shapes that are executed each frame, and the number of frames computed.
 */
public class Scene {
	// Member variables
	private Shape camera;
	private Screen screen;
	
	private ArrayList<Shape> shapes;
	private ArrayList<Shape> lights;
	
	private ArrayList<Action> actions;
	
	private int frames;
	private int maxFrames;
	
	// Constructors
	public Scene(String path) throws UndefinedException {
		File setup = new File(path);
		Scanner scanner = null;
		
		try {scanner = new Scanner(setup);}
		catch (FileNotFoundException e) {throw new UndefinedException(path);}
		
		shapes = new ArrayList<>();
		lights = new ArrayList<>();
		actions = new ArrayList<>();
		
		ArrayList<Shape> savedShapes = new ArrayList<>();
		ArrayList<String> savedNames = new ArrayList<>();
		
		frames = 0;
		maxFrames = -1;
		
		boolean commented = false;
		
		// Each line of the file is parsed individually
		// Comments, empty lines, and unfamiliar lines are skipped, and everything else is parsed
		for (int i = 1; scanner.hasNextLine(); i++) {
			String[] line = (i + " " + scanner.nextLine()).split(" ");
			
			if (line.length < 2) continue;
			
			if (line[1].equals("/*")) commented = true;
			if (line[1].equals("*/")) commented = false;
			
			if (commented || line[1].substring(0, 2).equals("//") || line[1].equals("\n")) continue;
			
			try {
				switch (line[1].toLowerCase()) {
					case "camera":
					parseCamera(line);
					break;
					
					case "screen":
					parseScreen(line);
					break;
					
					case "gif":
					parseGif(line);
					break;
					
					case "action":
					parseAction(line, savedShapes, savedNames);
					break;
					
					case "light":
					parseLight(line);
					break;
					
					default:
					parseShape(line, savedShapes, savedNames);
				}
			}
			catch (InvalidSetupException e) {System.out.println(e);}
			
			if (line[line.length - 1].equals("/*")) commented = true;
			if (line[line.length - 1].equals("*/")) commented = false;
		}
		
		scanner.close();
		
		if (camera == null) throw new UndefinedException("camera");
		if (screen == null) throw new UndefinedException("screen");
	}
	
	// Methods
	public void updateScreen() {
		screen.updateImage(this);
		frames++;
	}
	
	public void executeActions() {
		for (Action action : actions)
			action.execute();
	}
	
	// Returns an ArrayList of the shapes in the scene whose bounding spheres intersect the path of the ray
	public ArrayList<Shape> getVisible(Ray ray) {
		ArrayList<Shape> visible = new ArrayList<>();
		
		for (int i = 0; i < shapes.size(); i++) {
			if (ray.distToPoint(shapes.get(i).getPos()) < shapes.get(i).getBoundRadius() + Ray.MIN_LENGTH)
				visible.add(shapes.get(i));
		}
		
		return visible;
	}
	
	// Parsing methods
	private void parseCamera(String[] line) throws InvalidSetupException {
		if (line.length != 8) throw new InvalidSetupException(line);
		else camera = new Shape(getDoubles(line, 10, 2));
	}
	
	private void parseScreen(String[] line) throws InvalidSetupException {
		if (line.length != 8) throw new InvalidSetupException(line);
		else screen = new Screen(line[7], getInts(line, 5, 2));
	}
	
	private void parseGif(String[] line) throws InvalidSetupException {
		if (line.length != 3) throw new InvalidSetupException(line);
		maxFrames = Integer.parseInt(line[2]);
	}
	
	private void parseAction(String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		// Parsing the type of action
		int type = -1;
		
		for (int i = 0; i < Action.ACTION_TYPES.length; i++) {
			if (Action.ACTION_TYPES[i].equals(line[2])) {
				type = i;
				break;
			}
		}
		
		if (type == -1) throw new InvalidSetupException(line);
		
		// Parsing the values
		int numVecs = 0;
		switch (type) {
			case 0:
			numVecs = 1;
			break;
			
			case 1:
			numVecs = 1;
			break;
			
			case 2:
			numVecs = 2;
			break;
		}
		
		if (line.length != 4 + numVecs * 3) throw new InvalidSetupException(line);
		
		Vector[] vals = new Vector[numVecs];
		for (int i = 0; i < vals.length; i++)
			vals[i] = new Vector(getDoubles(line, 3, 4 + i * 3));
		
		// Parsing the shape that is acted upon
		int index = savedNames.indexOf(line[3]);
		
		if (index != -1)
			actions.add(new Action(type, savedShapes.get(index), vals));
		
		else if (line[3].toLowerCase().equals("camera"))
			actions.add(new Action(type, camera, vals));
		
		else
			System.out.println("\"" + line[3] + "\" is referenced at line " + line[0] + " but does not exist");
	}
	
	private void parseLight(String[] line) throws InvalidSetupException {
		if (line.length != 6) throw new InvalidSetupException(line);
		
		int k = 2;
		Shape light = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
								0, 0, 0, 0, 0, 0, Integer.decode(line[k++]));
		
		lights.add(light);
		shapes.add(light);
	}
	
	private Group parseGroup(String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		Group shape = new Group(getDoubles(line, 1, 4), getDefaultParams(line, 5));
		
		String[] groupShapes = line[3].split(",");
		
		for (int i = 0; i < groupShapes.length; i++) {
			char modifier = groupShapes[i].charAt(0);
			
			if (modifier == '+' || modifier == '-' || modifier == '&')
				groupShapes[i] = groupShapes[i].substring(1, groupShapes[i].length());
			else modifier = '+';
			
			int index = savedNames.indexOf(groupShapes[i]);
			
			if (index != -1) shape.add(savedShapes.get(index), modifier);
			else System.out.println("\"" + groupShapes[i] + "\" is referenced at line " + line[0] + " but does not exist");
		}
		
		return shape;
	}
	
	private void parseShape(String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		Shape shape;
		
		switch (line[1].toLowerCase()) {
			case "group":
			shape = parseGroup(line, savedShapes, savedNames);
			break;
			
			case "sphere":
			shape = new Sphere(getDoubles(line, 1, 3), getDefaultParams(line, 4));
			break;
			
			case "box":
			shape = new Box(getDoubles(line, 4, 3), getDefaultParams(line, 7));
			break;
			
			case "cylinder":
			shape = new Cylinder(getDoubles(line, 3, 3), getDefaultParams(line, 6));
			break;
			
			case "plane":
			shape = new Plane(getDoubles(line, 2, 3), getDefaultParams(line, 5));
			break;
			
			case "torus":
			shape = new Torus(getDoubles(line, 2, 3), getDefaultParams(line, 5));
			break;
			
			default:
			return;
		}
		
		if (line[2].equals(".")) shapes.add(shape);
		else {
			savedShapes.add(shape);
			savedNames.add(line[2]);
		}
	}
	
	// Getters
	public Shape getCamera() {return camera;}
	public Screen getScreen() {return screen;}
	
	public ArrayList<Shape> getShapes() {return shapes;}
	public ArrayList<Shape> getLights() {return lights;}
	
	public Shape getShape(int i) {return shapes.get(i);}
	public Shape getLight(int i) {return lights.get(i);}
	
	public int getFrames() {return frames;}
	public int getMaxFrames() {return maxFrames;}
	
	// Helpers
	private double[] getDefaultParams(String[] line, int offset) throws InvalidSetupException {
		if (line.length != Shape.DEFAULT_PARAMS.length + offset) throw new InvalidSetupException(line);
		
		double[] params = new double[Shape.DEFAULT_PARAMS.length];
		
		int i;
		for (i = 0; i < Shape.DEFAULT_PARAMS.length - 1; i++)
			params[i] = Double.parseDouble(line[i + offset]);
		
		params[i] = Integer.decode(line[i + offset]);
		
		return params;
	}
	
	private double[] getDoubles(String[] line, int doubles, int offset) {
		double[] input = new double[doubles];
		
		for (int i = 0; i < doubles && i + offset < line.length; i++)
			input[i] = Double.parseDouble(line[i + offset]);
		
		return input;
	}
	
	private int[] getInts(String[] line, int ints, int offset) {
		int[] input = new int[ints];
		
		for (int i = 0; i < ints && i + offset < line.length; i++)
			input[i] = Integer.decode(line[i + offset]);
		
		return input;
	}
}