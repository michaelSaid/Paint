package eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyShape;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends MyShape {
	private Double height = 0.0;
	private Double width = 0.0;
	public Rectangle(Point startPoint,Point endPoint)
	{
		super();
		Double x1 = Math.min(startPoint.getX(), endPoint.getX());
		Double y1 = Math.min(startPoint.getY(), endPoint.getY());
		this.height = Math.abs(endPoint.getY()-startPoint.getY());
		this.width = Math.abs(endPoint.getX()-startPoint.getX());
		getProperties().put("height", this.height);
		getProperties().put("width", this.width);
		this.getProperties().put("x1", x1);
		this.getProperties().put("y1", y1);
		setPosition(new Point(x1.intValue(),y1.intValue()));
	}
	public Rectangle()
	{
		super();
	}
	@Override
	public void draw(Object canvas) {
		// TODO Auto-generated method stub
		GraphicsContext g =  
	             ((Canvas) canvas).getGraphicsContext2D(); 
		g.setFill((Color)getFillColor());
		g.setStroke((Color)getColor());		
		g.setLineWidth(getProperties().get("stroke"));
		Point p1 = new Point(getProperties().get("x1").intValue(), getProperties().get("y1").intValue());
		this.width = getProperties().get("width");
		this.height=getProperties().get("height");
		g.fillRect(p1.getX(), p1.getY(), this.width,this.height);
		g.strokeRect(p1.getX(), p1.getY(), this.width,this.height);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape cloneObject = new Rectangle();
		Map<String,Double>proClone = new HashMap<>();
		proClone.clear();
		proClone.putAll(this.getProperties());
		cloneObject.setProperties(proClone);
		cloneObject.setPosition(getPosition());
		return cloneObject;
	}
	@Override
	public boolean isCountainsPoint(int x, int y) {
		// TODO Auto-generated method stub
		this.width = getProperties().get("width");
		this.height=getProperties().get("height");
		return x>=((Point)getPosition()).x&&y>=((Point)getPosition()).y
				&&x<((Point)getPosition()).x+this.width&&y<((Point)getPosition()).y+this.height;
	}
	@Override
	public Point[] getBonds() {
		// TODO Auto-generated method stub
		this.width = getProperties().get("width");
		this.height=getProperties().get("height");
		Point p1 = new Point(getProperties().get("x1").intValue(),
				getProperties().get("y1").intValue());
		Point p4 = new Point(getProperties().get("x1").intValue() + this.width.intValue(),
				getProperties().get("y1").intValue() + this.height.intValue());
		Point p2 = new Point(p1.x + (p4.x - p1.x), p1.y);
		Point p3 = new Point(p1.x, p1.y + (p4.y - p1.y));
		return new Point[] { p1, p2, p3, p4 };
	}
	@Override
	public void reSizeBy(int x, int y) {
		// TODO Auto-generated method stub
		this.width = getProperties().get("width");
		this.height=getProperties().get("height");
		this.width = Math.abs(this.width+=x);
		this.height =Math.abs(this.height+=y);
		getProperties().put("height", this.height);
		getProperties().put("width", this.width);
	}
	/**
	 * @return the height
	 */
	public Double getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(Double height) {
		this.height = height;
		getProperties().put("height", this.height);
		
	}
	/**
	 * @return the width
	 */
	public Double getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(Double width) {
		this.width = width;
		getProperties().put("width", this.width);
	}

}
