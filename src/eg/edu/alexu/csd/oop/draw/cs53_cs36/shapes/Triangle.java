package eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyShape;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends MyShape {

	public Triangle(Point p1,Point p2,Point p3)
	{
		super();
		this.getProperties().put("x1", p1.getX());
		this.getProperties().put("x2", p2.getX());
		this.getProperties().put("x3", p3.getX());
		this.getProperties().put("y1", p1.getY());
		this.getProperties().put("y2", p2.getY());
		this.getProperties().put("y3", p3.getY());
		this.setPosition(p1);
	}
	public Triangle()
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
		Point p1 = new Point(getProperties().get("x1").intValue(), getProperties().get("y1").intValue());
		Point p2 = new Point(getProperties().get("x2").intValue(), getProperties().get("y2").intValue());
		Point p3 = new Point(getProperties().get("x3").intValue(), getProperties().get("y3").intValue());
		g.fillPolygon(new double [] {p1.getX(),p2.getX(),p3.getX()} , new double [] {p1.getY(),p2.getY(),p3.getY()}, 3);
		g.strokePolygon(new double [] {p1.getX(),p2.getX(),p3.getX()} , new double [] {p1.getY(),p2.getY(),p3.getY()}, 3);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape cloneObject = new Triangle();
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
		Point p1 = new Point(getProperties().get("x1").intValue(), getProperties().get("y1").intValue());
		Point p2 = new Point(getProperties().get("x2").intValue(), getProperties().get("y2").intValue());
		Point p3 = new Point(getProperties().get("x3").intValue(), getProperties().get("y3").intValue());
		double totalA = calArea(p1, p2, p3);
		double a1 = calArea(new Point(x,y), p2, p3);
		double a2 = calArea(p1, new Point(x,y), p3);
		double a3 = calArea(p1, p2, new Point(x,y));
		return (a1+a2+a3)==totalA;
	}
	private double calArea(Point p1,Point p2,Point p3) {
		return  Math.abs((p1.x*(p2.y-p3.y)+p2.x*(p3.y-p1.y)+p3.x*(p1.y-p2.y))/2.0);
	}
	@Override
	public void moveBy(int x, int y) {
		getProperties().put("x1", (double) (((Point)getPosition()).x+x));
		getProperties().put("y1", (double) (((Point)getPosition()).y+y));
		getProperties().put("x2", (double) ((getProperties().get("x2")+x)));
		getProperties().put("y2", (double) ((getProperties().get("y2")+y)));
		getProperties().put("x3", (double) ((getProperties().get("x3")+x)));
		getProperties().put("y3", (double) ((getProperties().get("y3")+y)));
		setPosition(new Point(((Point)getPosition()).x+x,((Point)getPosition()).y+y));
	}
	@Override
	public Point[] getBonds() {
		// TODO Auto-generated method stub
		int x1 = getProperties().get("x1").intValue();
		int x2 = getProperties().get("x2").intValue();
		int x3 = getProperties().get("x3").intValue();
		int y1 = getProperties().get("y1").intValue();
		int y2 = getProperties().get("y2").intValue();
		int y3 = getProperties().get("y3").intValue();
		Point p1 = new Point(Math.min(Math.min(x1, x2), x3), Math.min(Math.min(y1, y2), y3));
		Point p4 = new Point(Math.max(Math.max(x1, x2), x3), Math.max(Math.max(y1, y2), y3));
		Point p2 = new Point(p1.x + (p4.x - p1.x), p1.y);
		Point p3 = new Point(p1.x, p1.y + (p4.y - p1.y));
		return new Point[] { p1, p2, p3, p4 };
	}
	@Override
	public void reSizeBy(int x, int y) {
		// TODO Auto-generated method stub
		getProperties().put("x3", (double) ((getProperties().get("x3")+x)));
		getProperties().put("y3", (double) ((getProperties().get("y3")+y)));
		getProperties().put("x2", (double) ((getProperties().get("x2")+x)));
		getProperties().put("y2", (double) ((getProperties().get("y2")+y)));
	}
}
