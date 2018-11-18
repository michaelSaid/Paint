package eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Square extends Rectangle {

	private Double length=0.0;
	public Square(Point startPoint,Point endPoint)
	{
		super(startPoint,endPoint);
		this.length = Math.max(this.getWidth(), this.getHeight());
		Double x = startPoint.x > endPoint.x ? startPoint.x - this.length : startPoint.x;
		Double y = startPoint.y > endPoint.y ? startPoint.y - this.length : startPoint.y;
		this.setHeight(this.length);
		this.setWidth(this.length);
		this.getProperties().put("x1", x);
		this.getProperties().put("y1", y);
		this.setPosition(new Point(x.intValue(),y.intValue()));
	}
	public Square()
	{
		super();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape cloneObject = new Square();
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
			this.length-=shift;
		else
			this.length+=shift;
		this.setHeight(this.length);
		this.setWidth(this.length);
		
	}

}
