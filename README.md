# Raymarching

This is a raymarching 3D rendering project made in Java. It uses the raymarching technique to render primitive shapes and calculate reflections and lighting.

# Using the setup files

setup.txt is the default setup file for this 3D Rendering engine. Render your own scenes by creating a new .txt file and sending the file name to the program as an argument when you run it. Lines are parsed individually, so every parameter for a shape must be included in the same line. All angles are in radians. The camera and screen must be defined in the file.

The camera must be defined as:
	camera [x] [y] [z] [phi] [theta] [psi]
Where [x], [y], and [z] are the coordinates of the camera and [phi], [theta], and [psi] are the euler angles of the camera's rotation.

The screen must be defined as:
	screen [width] [height] [distance] [ambientColor] [maxReflections] [name]
Where [width] and [height] are the dimensions of the window, [distance] is the distance from the user to the screen (in pixels, technically), [ambientColor] is the color and brightness of the ambient light in the scene, [maxReflections] is the maximum number of reflections a light ray can make, and [name] is the name of the window.

To save the render as a .gif, define it as:
	gif [frames]
Where frames is the number of frames to render.

Add shapes as:
	[type] [name] [parameters] [x] [y] [z] [phi] [theta] [psi] [shine] [transparency] [refractiveIndex] [color]
Where [type] is the type of shape. The types of shape are, sphere, box, plane, torus, and cylinder. [name] is the name of the shape. If the shape is referenced in the creation of a group by its name, it will not also be included in the scene. [parameters] represents the parameters specific to the type of shape being created, which must be space-separated. [x], [y], and [z] are the coordinates of the shape. [phi], [theta], and [psi] are the euler angles of the shape. [shine] is the percentage of light that is reflected off the surface from 0 to 1. [transparency] is the percentage of light that refracts through the object from 0 to 1. [refractiveIndex] is the refractive index of the shape, which only applies when the shape is not opaque. [color] is the color of the shape.
