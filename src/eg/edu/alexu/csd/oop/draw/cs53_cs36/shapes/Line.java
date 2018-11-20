package eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyShape;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends MyShape {

	public Line(Point p1,Point p2)
	{
		super();
		this.getProperties().put("x1", p1.getX());
		this.getProperties().put("x2", p2.getX());
		this.getProperties().put("y1", p1.getY());
		this.getProperties().put("y2", p2.getY());
		this.setPosition(p1);
	}
	public Line()
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
		g.setLineWidth(getProperties().get("stroke"));
		g.strokeLine(p1.x,p1.y,p2.x,p2.y);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape cloneObject = new Line();
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
		double slope = getProperties().get("y2")-getProperties().get("y1");
		slope = slope/(getProperties().get("x2")-getProperties().get("x1"));
		int yOfx = (int) ((x-getProperties().get("x1"))*slope + getProperties().get("y1"));
		return y>=yOfx-8&&y<=yOfx+8;
	}
	@Override
	public void moveBy(int x, int y) {
		setPosition(new Point(((Point)getPosition()).x+x,((Point)getPosition()).y+y));
		getProperties().put("x2", (double) ((getProperties().get("x2")+x)));
		getProperties().put("y2", (double) ((getProperties().get("y2")+y)));
	}
	@Override
	public Point[] getBonds() {
		// TODO Auto-generated method stub
		Point p1 = new Point();
		Point p4 = new Point();
		int x1 = this.getProperties().get("x1").intValue();
		int x2 = this.getProperties().get("x2").intValue();
		int y1 = this.getProperties().get("y1").intValue();
		int y2 = this.getProperties().get("y2").intValue();
		p1.setLocation(Math.min(x1, x2), Math.min(y1, y2));
		p4.setLocation(Math.max(x1, x2), Math.max(y1, y2));
		Point p2 = new Point(p1.x + (p4.x - p1.x), p1.y);
		Point p3 = new Point(p1.x, p1.y + (p4.y - p1.y));
		return new Point[] { p1, p2, p3, p4 };
	}
	@Override
	public void reSizeBy(int x, int y) {
		// TODO Auto-generated method stub
		getProperties().put("x2", (double) ((getProperties().get("x2")+x)));
		getProperties().put("y2", (double) ((getProperties().get("y2")+y)));
	}

}
