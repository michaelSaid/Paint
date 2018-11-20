package eg.edu.alexu.csd.oop.draw.cs53_cs36.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class MyShape implements Shape {

	private Point position;
	private Map<String,Double>properties;
	private Color defaultColor = Color.BLACK;
	private Color defaultFillColor = Color.WHITE;
	public MyShape() {
		// TODO Auto-generated constructor stub
		properties = new HashMap<>();
		properties.put("color", Double.valueOf(getRGB(defaultColor)));
		properties.put("fillcolor", Double.valueOf(getRGB(defaultFillColor)));
		getProperties().put("x1", 0.0);
		getProperties().put("y1", 0.0);
	}
	@Override
	public void setPosition(Object position) {
		// TODO Auto-generated method stub
		getProperties().put("x1", ((Point) position).getX());
		getProperties().put("y1", ((Point) position).getY());
		this.position = (Point) position;
	}

	@Override
	public Object getPosition() {
		// TODO Auto-generated method stub
		Point p = new Point();
		p.setLocation(this.getProperties().get("x1"), this.getProperties().get("y1"));
		this.position=p;
		return position;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		// TODO Auto-generated method stub
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		// TODO Auto-generated method stub
		return properties;
	}

	@Override
	public void setColor(Object color) {
		// TODO Auto-generated method stub
		this.properties.put("color", Double.valueOf((getRGB((Color)color))));
	}

	@Override
	public Object getColor() {
		// TODO Auto-generated method stub
		return toColor(this.properties.get("color").intValue());
	}

	@Override
	public void setFillColor(Object color) {
		// TODO Auto-generated method stub
		this.properties.put("fillcolor", Double.valueOf((getRGB((Color)color))));

	}

	@Override
	public Object getFillColor() {
		// TODO Auto-generated method stub
		return toColor(this.properties.get("fillcolor").intValue());
	}

	@Override
	public abstract void draw(Object canvas);
	
	public void moveBy(int x, int y) {
		getProperties().put("x1", (double) (((Point)getPosition()).x+x));
		getProperties().put("y1", (double) (((Point)getPosition()).y+y));
		setPosition(new Point(((Point)getPosition()).x+x,((Point)getPosition()).y+y));
	}
	public abstract void reSizeBy(int x, int y);
	public abstract boolean isCountainsPoint(int x, int y);
	@Override
	public abstract Object clone() throws CloneNotSupportedException;
	private int getRGB(Color col) {
	    int r = ((int)col.getRed()*255);
	    int g = ((int)col.getGreen() * 255);
	    int b = ((int)col.getBlue() * 255);
	    int rgb = (r << 16) + (g << 8) + b;
	    return rgb;
	}
	private Color toColor(int rgb) {
		java.awt.Color awtColor = new java.awt.Color(rgb);
		int r = awtColor.getRed();
		int g = awtColor.getGreen();
		int b = awtColor.getBlue();
		int a = awtColor.getAlpha();
		double opacity = a / 255.0 ;
		return Color.rgb(r, g, b,opacity);
		
	}
	public abstract Point[] getBonds();
	
	public void drawBonds(Object canvas) {
		int margin = 5;
		GraphicsContext g =  
	             ((Canvas) canvas).getGraphicsContext2D(); 
		Point[] bonds = getBonds();
		g.strokeRect(bonds[0].x - margin, bonds[0].y - margin, bonds[3].x - bonds[0].x + 2 * margin,
			bonds[3].y - bonds[0].y + 2 * margin);

	}
}
