package raymarching;

import raymarching.exceptions.InvalidSetupException;
import raymarching.exceptions.UndefinedException;

import raymarching.shapes.*;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*
 * A Scene object contains the camera, screen, and all of the shapes and lights that are being rendered in the scene.
 * It also stores the movements and rotations of all the shapes that are executed each frame, and the number of frames computed.
 */
public class Scene {
	// Member variables
	private Camera camera;
	private Screen screen;
	
	private ArrayList<Shape> shapes;
	private ArrayList<Light> lights;
	
	private ArrayList<Action> actions;
	
	private int frames;
	private int maxFrames;
	
	private int outputFormat = 0;
	
	private boolean suppressWarnings;
	
	// Constructors
	public Scene(String path) throws UndefinedException {
		File setup = new File("scenes\\" + path);
		Scanner scanner = null;
		
		try {scanner = new Scanner(setup);}
		catch (FileNotFoundException e) {throw new UndefinedException(path);}
		
		shapes = new ArrayList<>();
		lights = new ArrayList<>();
		
		actions = new ArrayList<>();
		
		frames = 0;
		maxFrames = -1;
		
		scanFile(scanner);
		scanner.close();
		
		if (camera == null) throw new UndefinedException("camera");
		if (screen == null) throw new UndefinedException("screen");
	}
	
	// Methods
	public void next() {
		screen.updateImage(this);
		
		for (Action action : actions)
			action.execute();
		
		frames++;
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
	
	// Getters
	public Camera getCamera() {return camera;}
	public Screen getScreen() {return screen;}
	
	public ArrayList<Shape> getShapes() {return shapes;}
	public ArrayList<Light> getLights() {return lights;}
	
	public Shape getShape(int i) {return shapes.get(i);}
	public Light getLight(int i) {return lights.get(i);}
	
	public int getFrames() {return frames;}
	public int getMaxFrames() {return maxFrames;}
	
	public int getOutputFormat() {return outputFormat;}
	
	// Parsing methods
	private void scanFile(Scanner scanner) {
		ArrayList<Shape> removedShapes = new ArrayList<>();
		ArrayList<String> savedShapes = new ArrayList<>();
		
		boolean commented = false;
		suppressWarnings = false;
		
		// Each line of the file is parsed individually
		// Comments and empty lines are skipped, everything else is parsed
		for (int i = 1; scanner.hasNextLine(); i++) {
			String[] line = (i + " " + scanner.nextLine()).split(" ");
			
			if (line.length < 2) continue;
			
			if (line[1].equals("/*")) commented = true;
			if (line[1].equals("*/")) commented = false;
			
			if (commented || line[1].equals("*/") || line[1].substring(0, 2).equals("//") || line[1].equals("\n")) continue;
			
			try {
				switch (line[1].toLowerCase()) {
					case "suppress_warnings":
					InvalidSetupException.assertLength(line, 3);
					suppressWarnings = Boolean.parseBoolean(line[2].toLowerCase());
					break;
					
					case "gif":
					InvalidSetupException.assertLength(line, 3);
					maxFrames = Integer.parseInt(line[2]);
					outputFormat = 1;
					break;
					
					case "png":
					InvalidSetupException.assertLength(line, 2);
					outputFormat = 2;
					break;
					
					case "screen":
					screen = parseScreen(line);
					break;
					
					case "camera":
					camera = parseCamera(line);
					break;
					
					case "light":
					Light light = parseLight(line);
					lights.add(light);
					shapes.add(light);
					savedShapes.add(line[2]);
					break;
					
					case "action":
					Action action = parseAction(line, savedShapes);
					if (action != null) actions.add(action);
					break;
					
					case "group":
					shapes.add(parseGroup(line, removedShapes, savedShapes));
					savedShapes.add(line[2]);
					break;
					
					default:
					Shape shape = parseShape(line);
					if (shape != null) {
						shapes.add(shape);
						savedShapes.add(line[2]);
					}
				}
			}
			catch (InvalidSetupException e) {System.out.println(e);}
			
			if (line[line.length - 1].equals("/*")) commented = true;
			if (line[line.length - 1].equals("*/")) commented = false;
		}
		
		for (Shape shape : removedShapes)
			shapes.remove(shape);
	}
	
	private Screen parseScreen(String[] line) throws InvalidSetupException {
		InvalidSetupException.assertLength(line, 7);
		return new Screen(getInts(line, 2, 5));
	}
	
	private Camera parseCamera(String[] line) throws InvalidSetupException {
		InvalidSetupException.assertLength(line, 8);
		return new Camera(getDoubles(line, 2, 6));
	}
	
	private Light parseLight(String[] line) throws InvalidSetupException {
		InvalidSetupException.assertLength(line, 9);
		
		return new Light(getDoubles(line, 3, 6));
	}
	
	private Action parseAction(String[] line, ArrayList<String> savedShapes) throws InvalidSetupException {
		// Parsing the type of action
		int type = -1;
		
		for (int i = 0; i < Action.ACTION_TYPES.length; i++) {
			if (Action.ACTION_TYPES[i].equals(line[2])) {
				type = i;
				break;
			}
		}
		
		if (type == -1) throw new InvalidSetupException(line);
		
		int numVecs = Action.NUM_VECS_BY_TYPE[type];
		
		InvalidSetupException.assertLength(line, 4 + numVecs * 3);
		
		Vector[] vals = new Vector[numVecs];
		for (int i = 0; i < numVecs; i++)
			vals[i] = new Vector(getDoubles(line, 4 + i * 3, 3));
		
		// Parsing the shape that is acted upon
		int index = savedShapes.indexOf(line[3]);
		
		if (index != -1)
			return new Action(type, shapes.get(index), vals);
		
		else if (line[3].toLowerCase().equals("camera"))
			return new Action(type, camera, vals);
		
		else if (!suppressWarnings)
			System.out.println("\"" + line[3] + "\" is referenced at line " + line[0] + " but does not exist");
		
		return null;
	}
	
	private Group parseGroup(String[] line, ArrayList<Shape> removedShapes, ArrayList<String> savedShapes) throws InvalidSetupException {
		InvalidSetupException.assertLength(line, 4 + Group.PARAMS.length + Shape.DEFAULT_PARAMS.length);
		
		Group shape = new Group(getDoubles(line, 4, Group.PARAMS.length), getDoubles(line, 4 + Group.PARAMS.length, Shape.DEFAULT_PARAMS.length));
		
		String[] groupShapes = line[3].split(",");
		
		for (int i = 0; i < groupShapes.length; i++) {
			char modifier = groupShapes[i].charAt(0);
			
			if (modifier == '+' || modifier == '-' || modifier == '&')
				groupShapes[i] = groupShapes[i].substring(1, groupShapes[i].length());
			else modifier = '+';
			
			if (i == 0 && modifier != '+') {
				modifier = '+';
				if (!suppressWarnings) System.out.println("\"" + groupShapes[i] + "\" modifier automatically changed to \"+\" at line " + line[0]);
			}
			
			int index = savedShapes.indexOf(groupShapes[i]);
			
			if (index != -1) {
				shape.add(shapes.get(index), modifier);
				removedShapes.add(shapes.get(index));
			}
			else if (!suppressWarnings) System.out.println("\"" + groupShapes[i] + "\" is referenced at line " + line[0] + " but does not exist");
		}
		
		return shape;
	}
	
	private Shape parseShape(String[] line) throws InvalidSetupException {
		if (line.length < 2) throw new InvalidSetupException(line);
		
		Class<?> type = null;
		try {
			type = Class.forName("raymarching.shapes." + line[1].substring(0, 1).toUpperCase() + line[1].substring(1).toLowerCase());
		}
		catch (ClassNotFoundException e) {
			System.out.println(e);
			return null;
		}
		
		int numParams = 0;
		try {
			numParams = String[].class.cast(type.getField("PARAMS").get(null)).length;
		}
		catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		InvalidSetupException.assertLength(line, 4 + numParams + Shape.DEFAULT_PARAMS.length);
		
		Shape shape = null;
		try {
			shape = Shape.class.cast(type.getConstructor(double[].class, double[].class)
										 .newInstance(getDoubles(line, 4, numParams), getDoubles(line, 4 + numParams, Shape.DEFAULT_PARAMS.length)));
		}
		catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		try {shape.setColor(Integer.decode(line[3]));}
		catch (NumberFormatException e) {}
		
		try {shape.loadTexture("textures\\" + line[3]);}
		catch (IOException e) {}
		
		return shape;
	}
	
	private double[] getDoubles(String[] line, int start, int length) {
		double[] doubles = new double[length];
		
		for (int i = 0; i < length && i + start < line.length; i++)
			doubles[i] = Double.parseDouble(line[i + start]);
		
		return doubles;
	}
	
	private int[] getInts(String[] line, int start, int length) {
		int[] ints = new int[length];
		
		for (int i = 0; i < length && i + start < line.length; i++)
			ints[i] = Integer.decode(line[i + start]);
		
		return ints;
	}
}
