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
	public Scene(String path) throws FileNotFoundException, InvalidSetupException {
		File setup = new File(path);
		Scanner scanner = new Scanner(setup);
		
		shapes = new ArrayList<>();
		lights = new ArrayList<>();
		
		ArrayList<Shape> savedShapes = new ArrayList<>();
		ArrayList<String> savedNames = new ArrayList<>();
		
		for (int i = 1; scanner.hasNextLine(); i++) {
			String rawLine = scanner.nextLine();
			String[] line = rawLine.split(" ");
			
			if (line.length < 2 || line[0].substring(0, 2).equals("\\\\") || line[0].equals("\n")) continue;
			
			switch (line[0].toLowerCase()) {
				case "camera":
				parseCamera(i, rawLine, line);
				break;
				
				case "screen":
				parseScreen(i, rawLine, line);
				break;
				
				case "light":
				parseLight(i, rawLine, line);
				break;
				
				case "group":
				parseGroup(i, rawLine, line, savedShapes, savedNames);
				break;
				
				case "sphere":
				parseSphere(i, rawLine, line, savedShapes, savedNames);
				break;
				
				case "box":
				parseBox(i, rawLine, line, savedShapes, savedNames);
				break;
				
				case "plane":
				parsePlane(i, rawLine, line, savedShapes, savedNames);
				break;
				
				case "cylinder":
				parseCylinder(i, rawLine, line, savedShapes, savedNames);
				break;
				
				case "torus":
				parseTorus(i, rawLine, line, savedShapes, savedNames);
				break;
			}
		}
		
		scanner.close();
	}
	
	// Methods
	public void updateScreen() {
		screen.updateImage(this);
	}
	
	// Parsing methods
	public void parseCamera(int i, String rawLine, String[] line) throws InvalidSetupException {
		if (line.length != 7) throw new InvalidSetupException(i, "Camera", rawLine, "x, y, z, phi, theta, psi");
		
		int k = 1;
		camera = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), 0, 0);
	}
	
	public void parseScreen(int i, String rawLine, String[] line) throws InvalidSetupException {
		if (line.length != 6) throw new InvalidSetupException(i, "Screen", rawLine, "width, height, distance, backgroundColor, title");
		
		int k = 1;
		screen = new Screen(Integer.parseInt(line[k++]), Integer.parseInt(line[k++]), Integer.parseInt(line[k++]), Integer.decode(line[k++]), line[k++]);
	}
	
	public void parseLight(int i, String rawLine, String[] line) throws InvalidSetupException {
		if (line.length != 5) throw new InvalidSetupException(i, "Light", rawLine, "x, y, z, color");
		
		int k = 1;
		Shape light = new Shape(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), 0, 0, 0, 0, Integer.decode(line[k++]));
		
		lights.add(light);
		shapes.add(light);
	}
	
	public void parseGroup(int i, String rawLine, String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		if (line.length != 12) throw new InvalidSetupException(i, "Group", rawLine, "shapeList, x, y, z, phi, theta, psi, smoothing, shine, color");
		
		int k = 3;
		Group group = new Group(new ArrayList<Shape>(), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Integer.decode(line[k++]));
		
		String[] groupShapes = line[2].split(",");
		
		for (int j = 0; j < groupShapes.length; j++) {
			int index = savedNames.indexOf(groupShapes[j]);
			
			if (index != -1) group.add(savedShapes.get(index));
			else System.out.println("\"" + groupShapes[j] + "\" does not exist\n " + i + "\t" + rawLine);
		}
		
		if (line[1].equals("-")) shapes.add(group);
		else {
			savedShapes.add(group);
			savedNames.add(line[1]);
		}
	}
	
	public void parseSphere(int i, String rawLine, String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		if (line.length != 8) throw new InvalidSetupException(i, "Sphere", rawLine, "radius, x, y, z, shine, color");
		
		int k = 2;
		Sphere sphere = new Sphere(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Integer.decode(line[k++]));
		
		if (line[1].equals("-")) shapes.add(sphere);
		else {
			savedShapes.add(sphere);
			savedNames.add(line[1]);
		}
	}
	
	public void parseBox(int i, String rawLine, String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		if (line.length != 14) throw new InvalidSetupException(i, "Box", rawLine, "width, height, length, radius, x, y, z, phi, theta, psi, shine, color");
		
		int k = 2;
		Box box = new Box(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Integer.decode(line[k++]));
		
		if (line[1].equals("-")) shapes.add(box);
		else {
			savedShapes.add(box);
			savedNames.add(line[1]);
		}
	}
	
	public void parsePlane(int i, String rawLine, String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		if (line.length != 12) throw new InvalidSetupException(i, "Plane", rawLine, "width, length, x, y, z, phi, theta, psi, shine, color");
		
		int k = 2;
		Plane plane = new Plane(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Integer.decode(line[k++]));
		
		if (line[1].equals("-")) shapes.add(plane);
		else {
			savedShapes.add(plane);
			savedNames.add(line[1]);
		}
	}
	
	public void parseCylinder(int i, String rawLine, String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		if (line.length != 13) throw new InvalidSetupException(i, "Cylinder", rawLine, "radius, height, edgeRadius, x, y, z, phi, theta, psi, shine, color");
		
		int k = 2;
		Cylinder cylinder = new Cylinder(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Integer.decode(line[k++]));
		
		if (line[1].equals("-")) shapes.add(cylinder);
		else {
			savedShapes.add(cylinder);
			savedNames.add(line[1]);
		}
	}
	
	public void parseTorus(int i, String rawLine, String[] line, ArrayList<Shape> savedShapes, ArrayList<String> savedNames) throws InvalidSetupException {
		if (line.length != 12) throw new InvalidSetupException(i, "Torus", rawLine, "largeRadius, smallRadius, x, y, z, phi, theta, psi, shine, color");
		
		int k = 2;
		Torus torus = new Torus(Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Double.parseDouble(line[k++]), Integer.decode(line[k++]));
		
		if (line[1].equals("-")) shapes.add(torus);
		else {
			savedShapes.add(torus);
			savedNames.add(line[1]);
		}
	}
	
	// Getters
	public Shape getCamera() {return camera;}
	public Screen getScreen() {return screen;}
	public ArrayList<Shape> getShapes() {return shapes;}
	public ArrayList<Shape> getLights() {return lights;}
}