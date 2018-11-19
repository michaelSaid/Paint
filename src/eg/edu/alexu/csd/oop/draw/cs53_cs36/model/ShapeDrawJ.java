package eg.edu.alexu.csd.oop.draw.cs53_cs36.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

public class ShapeDrawJ {
	private Shape shape = null;
	private DrawingEngine engine;

	public ShapeDrawJ() {};

	public ShapeDrawJ(DrawingEngine engine) {
		this.engine = engine;
	}

	
	public Shape createShape(String shape) {

		for (Class<? extends Shape> i : engine.getSupportedShapes()) {
			if (shape.equals(i.getSimpleName())) {
				try {
					Constructor<?> constuctor = null;
					try {
						constuctor = i.getConstructor();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}

					try {
						this.shape =  (Shape) constuctor.newInstance();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return this.shape;

				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return null;

	}

	public Shape createShape(Shape shape) {
		String shapeName = shape.getClass().getSimpleName();
		return createShape(shapeName);
	}
}