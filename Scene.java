import shapes.*;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Scene {
	// Member variables
	private Shape camera;
	private Screen screen;
	
	private ArrayList<Shape> shapes;
	private ArrayList<Shape> lights;
	
	// Constructors
	public Scene(String path) throws FileNotFoundException {
		File setup = new File(path);
		Scanner scanner = new Scanner(setup);
		
		shapes = new ArrayList<>();
		lights = new ArrayList<>();
		
		ArrayList<Shape> savedShapes = new ArrayList<>();
		ArrayList<String> savedNames = new ArrayList<>();
		
		for (int i = 1; scanner.hasNextLine(); i++) {
			String[] line = (i + " " + scanner.nextLine()).split(" ");
			
			if (line.length < 3 || line[1].substring(0, 2).equals("\\\\") || line[1].equals("\n")) continue;
			
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
	}
	
	// Parsing methods
	private void parseCamera(String[] line) {
		int k = 2;
		camera = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
						   Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), 0, 0, 0, 0);
	}
	
	private void parseScreen(String[] line) {
		int k = 2;
		screen = new Screen(Integer.parseInt(line[k++]), Integer.parseInt(line[k++]), Integer.parseInt(line[k++]),
							Integer.decode(line[k++]), line[k++]);
	}
	
	private void parseLight(String[] line) {
		int k = 2;
		Shape light = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]),
								0, 0, 0, 0, 0, 0, Integer.decode(line[k++]));
		
		lights.add(light);
		shapes.add(light);
	}
	
	private Group parseGroup(String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		Group shape = new Group(new ArrayList<Shape>(), Double.parseDouble(line[4]), getDefaultParams(line, 5));
		
		String[] groupShapes = line[3].split(",");
		
		for (int i = 0; i < groupShapes.length; i++) {
			int index = savedNames.indexOf(groupShapes[i]);
			
			if (index != -1) shape.add(savedShapes.get(index));
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
}