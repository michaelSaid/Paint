package eg.edu.alexu.csd.oop.draw.cs53_cs36.model;

import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MyPaint implements DrawingEngine {

	private LinkedList<Shape> MyShapes;
	private Stack<LinkedList<Shape>> undo;
	private Stack<LinkedList<Shape>> redo;
	private List<Class<? extends Shape>> supportedShapes;
	@SuppressWarnings("unchecked")
	public MyPaint() throws Exception{
		// TODO Auto-generated constructor stub
		MyShapes = new LinkedList<>();
		undo = new Stack<>();
		redo = new Stack<>();
		supportedShapes = new ArrayList<Class<? extends Shape>>();
		try {
			String[] shapesName = new String[] {"Line","Ellipse","Circle","Rectangle","Square","Triangle"};
			for(String name:shapesName)
			supportedShapes.add((Class<? extends Shape>) Class.forName("eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes."+name, true, this.getClass().getClassLoader()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		installPluginShape("RoundRectangle.jar");
		for(int i=0;i<supportedShapes.size();i++)
			System.out.println(supportedShapes.get(i).getName());
	}
	@Override
	public void refresh(Object canvas) {
		// TODO Auto-generated method stub
		if(canvas.getClass().getName().equals("javax.swing.DebugGraphics")) {
			canvas = (Graphics) canvas;
		}else {
		GraphicsContext g = ((Canvas)canvas).getGraphicsContext2D();
		g.clearRect(0, 0, ((Canvas)canvas).getWidth(), ((Canvas)canvas).getHeight());
		}
		for(int i=0;i< MyShapes.size();i++) {
			if(MyShapes.get(i).getClass().getName().equals("eg.edu.alexu.csd.oop.draw.RoundRectangle"))
				drawRounRect(canvas, MyShapes.get(i));
			else
				MyShapes.get(i).draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		// TODO Auto-generated method stub
		undoStore(MyShapes);
		MyShapes.add(shape);
		redoStore(MyShapes);
	}

	@Override
	public void removeShape(Shape shape) {
		// TODO Auto-generated method stub
		for(int i=0;i< MyShapes.size();i++) {
			
		if(shape.getProperties()!=null&&MyShapes.get(i).getProperties()!=null) {
			if(shape.getProperties().equals(MyShapes.get(i).getProperties())) {
				undoStore(MyShapes);
				MyShapes.remove(i);
				redoStore(MyShapes);
				return;
			}
		}else if(shape.getProperties()==null&&MyShapes.get(i).getProperties()==null)
		{
			undoStore(MyShapes);
			MyShapes.remove(i);
			redoStore(MyShapes);
			return;
		}
	}
}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		// TODO Auto-generated method stub
		for(int i=0;i< MyShapes.size();i++) {
		if(oldShape.getProperties()!=null&&MyShapes.get(i).getProperties()!=null) {
			if(oldShape.getProperties().equals(MyShapes.get(i).getProperties())) {
				undoStore(MyShapes);
				MyShapes.remove(i);
				MyShapes.add(i, newShape);
				redoStore(MyShapes);
				return;
			}
		}else if(oldShape.getProperties()==null&&MyShapes.get(i).getProperties()==null)
		{
			undoStore(MyShapes);
			MyShapes.remove(i);
			MyShapes.add(i, newShape);
			redoStore(MyShapes);
			return;
		}
	}
}

	@Override
	public Shape[] getShapes() {
		// TODO Auto-generated method stub
		Shape[] arShapes = new Shape[MyShapes.size()];
		MyShapes.toArray(arShapes);
		return arShapes;
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		// TODO Auto-generated method stub
		return this.supportedShapes;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		if(undo.isEmpty()) {
			return;
		}
		while(undo.peek().equals(MyShapes)) {
			redo.push(undo.pop());
			if (undo.isEmpty()) {
				return;
			}
		}
		redo.push(undo.peek());
		MyShapes=undo.pop();
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		if(redo.isEmpty()) {
			return;
		}
		while(redo.peek().equals(MyShapes)) {
			undo.push(redo.pop());
			if (redo.isEmpty()) {
				return;
			}
		}
		undo.push(redo.peek());
		MyShapes=redo.pop();
		
	}
	
	
	/*store the current shapes before any addition , removing or updating 
	  and check undo limited to 20 steps */	
    private void undoStore(LinkedList<Shape> x) {
		@SuppressWarnings("unchecked")
		LinkedList<Shape> copyMyShapes = (LinkedList<Shape>) x.clone();
    	undo.push(copyMyShapes);
    	if(undo.size()>20) {
    		undo.remove(0);
    	}
    }
    
    /*store the current shapes after any addition , removing or updating 
	  and check redo after any change must be once and forget the previous */
    private void redoStore(LinkedList<Shape> x) {
		@SuppressWarnings("unchecked")
		LinkedList<Shape> copyMyShapes = (LinkedList<Shape>) x.clone();
    	redo.push(copyMyShapes);
    	while(redo.size()>1) {
    		redo.remove(0);
    	}
    }

    @Override
	public void save(String path) {
		// TODO Auto-generated method stub
    	String s = path.substring(path.lastIndexOf("."));
    	if(s.equalsIgnoreCase(".xml")) {
    		FileManager.WriteXML(path, this);
    	}else {
    		try {
				FileManager.WriteJSON(path, this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

	}

	@Override
	public void load(String path) {
		// TODO Auto-generated method stub
			MyShapes = new LinkedList<Shape>();
	    	String s = path.substring(path.lastIndexOf("."));
	    	if(s.equalsIgnoreCase(".xml")) {
	    		FileManager.loadXML(path, this);
	    	}else {
	    		try {
					FileManager.loadJson(path, this);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	undo = new Stack<LinkedList<Shape>>();
	    	redo = new Stack<LinkedList<Shape>>();
					
	}
	@SuppressWarnings("unchecked")
	public void installPluginShape(String jarPath) {
		// TODO Auto-generated method stub
		try {
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> e = jarFile.entries();
			URL[] urls = {new URL("jar:file:" + jarPath+"!/")};
			URLClassLoader classLoader = URLClassLoader.newInstance(urls);
			while(e.hasMoreElements()){
				JarEntry je = e.nextElement();
				if(je.isDirectory()||!je.getName().endsWith(".class")){
					continue;
				}
			    String className = je.getName().substring(0,je.getName().length()-6);
				 className = className.replace('/', '.');
				    try {
						Class<?> c = classLoader.loadClass(className);
						supportedShapes.add((Class<? extends Shape>) c);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Shape createShape(String nameClass) throws Exception {
		if(nameClass.endsWith("eg.edu.alexu.csd.oop.test.DummyShape")) {
			
			Class<? extends Shape> x = (Class<? extends Shape>) Class.forName(nameClass);
			Constructor<?> ctor = x.getConstructor();
			Shape shape = (Shape) ctor.newInstance(new Object[] {});
			return shape;
		}
		for(Class<? extends Shape> x:this.supportedShapes) {
			if(x.getName().equals(nameClass)) {
				Constructor<?> ctor = x.getConstructor();
				Shape shape = (Shape) ctor.newInstance(new Object[] {});
				return shape;
			}
		}
		return null;
		
	}
	public Class<? extends Shape> createClass(String nameClass) {
		for(Class<? extends Shape> x:this.supportedShapes) {
			if(x.getName().equals(nameClass)) {
				return x;
			}
		}
		return null;
		
	}
	public void drawRounRect(Object canvas , Shape r) {
		GraphicsContext g = ((Canvas)canvas).getGraphicsContext2D();
		double x1 = ((Point)r.getPosition()).getX();
		double y1 = ((Point)r.getPosition()).getY();
		double width = r.getProperties().get("Width");
		double height = r.getProperties().get("Length");
		g.setLineWidth(r.getProperties().get("stroke"));
		g.setStroke(MyShape.toColor(((java.awt.Color)r.getColor()).getRGB()));
		g.setFill(MyShape.toColor(((java.awt.Color)r.getFillColor()).getRGB()));
		g.fillRoundRect(x1, y1, width, height, 40,40);
		g.strokeRoundRect(x1, y1, width, height, 40,40);
	}
}
