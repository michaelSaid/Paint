package eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Circle extends Ellipse {

	private Double raduis = 0.0;
	public Circle(Point startPoint,Point endPoint)
	{
		super(startPoint,endPoint);
		this.raduis = Math.sqrt(Math.pow(this.getWidth(), 2) + Math.pow(Math.abs(this.getHeight()), 2));
		this.setWidth(this.raduis*2);
		this.setHeight(this.raduis*2);
		setPosition(new Point((int)startPoint.x-raduis.intValue(),startPoint.y-raduis.intValue()));
	}
	public Circle()
	{
		super();
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape cloneObject = new Circle();
		Map<String,Double>proClone = new HashMap<>();
		proClone.clear();
		proClone.putAll(this.getProperties());
		cloneObject.setProperties(proClone);
		cloneObject.setPosition(getPosition());
		return cloneObject;
	}
	@Override
	public void reSizeBy(int x, int y) {
		// TODO Auto-generated method stub
		double shift = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		if(x<=0&&y<=0)
			this.raduis = Math.abs(this.raduis-=shift);
		else
			this.raduis+=shift;
		this.setWidth(this.raduis*2);
		this.setHeight(this.raduis*2);
	}

}
