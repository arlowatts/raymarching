package src;

import src.shapes.*;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Scene {
	// Constants
	private static final String[] actionTypes = {"translate", "rotate", "rotateabout"};
	
	// Member variables
	private Shape camera;
	private Screen screen;
	
	private ArrayList<Shape> shapes;
	private ArrayList<Shape> lights;
	
	private ArrayList<Action> actions;
	
	private int frames;
	private int maxFrames;
	
	// Constructors
	public Scene(String path) throws FileNotFoundException {
		File setup = new File(path);
		Scanner scanner = new Scanner(setup);
		
		shapes = new ArrayList<>();
		lights = new ArrayList<>();
		actions = new ArrayList<>();
		
		ArrayList<Shape> savedShapes = new ArrayList<>();
		ArrayList<String> savedNames = new ArrayList<>();
		
		frames = 0;
		maxFrames = -1;
		
		boolean commented = false;
		
		for (int i = 1; scanner.hasNextLine(); i++) {
			String[] line = (i + " " + scanner.nextLine()).split(" ");
			
			if (line.length < 2) continue;
			
			if (line[1].equals("/*")) commented = true;
			if (line[1].equals("*/")) commented = false;
			
			if (commented || line[1].substring(0, 2).equals("//") || line[1].equals("\n")) continue;
			
			switch (line[1].toLowerCase()) {
				case "camera":
				parseCamera(line);
				break;
				
				case "screen":
				parseScreen(line);
				break;
				
				case "light":
				parseLight(line);
				break;
				
				case "gif":
				parseGif(line);
				break;
				
				case "action":
				parseAction(line, savedShapes, savedNames);
				break;
				
				default:
				try {parseShape(line, savedShapes, savedNames);}
				catch (InvalidSetupException e) {System.out.println(e);}
			}
		}
		
		scanner.close();
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
	
	public ArrayList<Shape> getVisible(Ray ray) {
		ArrayList<Shape> visible = new ArrayList<>();
		
		for (int i = 0; i < shapes.size(); i++) {
			if (ray.distToPoint(shapes.get(i).getPos()) < shapes.get(i).getBoundRadius() + Ray.MIN_LENGTH)
				visible.add(shapes.get(i));
		}
		
		return visible;
	}
	
	// Parsing methods
	private void parseAction(String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) {
		int type = -1;
		
		for (int i = 0; i < actionTypes.length; i++) {
			if (actionTypes[i].equals(line[2].toLowerCase())) {
				type = i;
				break;
			}
		}
		
		if (type == -1) return;
		
		int index = savedNames.indexOf(line[3]);
		
		Vector[] vals = new Vector[(line.length - 3) / 3];
		for (int i = 0; i < vals.length; i++)
			vals[i] = new Vector(Double.parseDouble(line[i + 4]), Double.parseDouble(line[i + 5]), Double.parseDouble(line[i + 6]));
		
		if (index != -1)
			actions.add(new Action(type, savedShapes.get(index), vals));
		
		else if (line[3].toLowerCase().equals("camera"))
			actions.add(new Action(type, camera, vals));
		
		else
			System.out.println("\"" + line[3] + "\" is referenced at line " + line[0] + " but does not exist");
	}
	
	private void parseCamera(String[] line) {
		int k = 2;
		camera = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
						   Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), 0, 0, 0, 0);
	}
	
	private void parseScreen(String[] line) {
		int k = 2;
		screen = new Screen(Integer.parseInt(line[k++]), Integer.parseInt(line[k++]), Integer.parseInt(line[k++]),
							Integer.decode(line[k++]), Integer.parseInt(line[k++]), line[k++]);
	}
	
	private void parseLight(String[] line) {
		int k = 2;
		Shape light = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
								0, 0, 0, 0, 0, 0, Integer.decode(line[k++]));
		
		lights.add(light);
		shapes.add(light);
	}
	
	private void parseGif(String[] line) {
		maxFrames = Integer.parseInt(line[2]);
	}
	
	private Group parseGroup(String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		Group shape = new Group(new ArrayList<Shape>(), new ArrayList<Character>(), Double.parseDouble(line[4]), getDefaultParams(line, 5));
		
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
		
		int k = 3;
		
		switch (line[1].toLowerCase()) {
			case "group":
			shape = parseGroup(line, savedShapes, savedNames);
			break;
			
			case "sphere":
			shape = new Sphere(Double.parseDouble(line[k++]), getDefaultParams(line, k));
			break;
			
			case "box":
			shape = new Box(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
							Double.parseDouble(line[k++]), getDefaultParams(line, k));
			break;
			
			case "cylinder":
			shape = new Cylinder(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
								 getDefaultParams(line, k));
			break;
			
			case "plane":
			shape = new Plane(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), getDefaultParams(line, k));
			break;
			
			case "torus":
			shape = new Torus(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), getDefaultParams(line, k));
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
	
	private double[] getDefaultParams(String[] line, int offset) throws InvalidSetupException {
		if (line.length != Shape.DEFAULT_PARAMS.length + offset) throw new InvalidSetupException(line);
		
		double[] params = new double[Shape.DEFAULT_PARAMS.length];
		
		int i;
		for (i = 0; i < Shape.DEFAULT_PARAMS.length - 1; i++)
			params[i] = Double.parseDouble(line[i + offset]);
		
		params[i] = Integer.decode(line[i + offset]);
		
		return params;
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
}