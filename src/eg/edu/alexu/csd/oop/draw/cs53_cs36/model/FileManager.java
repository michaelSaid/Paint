package eg.edu.alexu.csd.oop.draw.cs53_cs36.model;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eg.edu.alexu.csd.oop.draw.Shape;

public class FileManager {
	
	private FileManager() {
		
	}
	public static void WriteXML(String path , MyPaint paint) {
		try {
			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<paint>");
			Shape[] shapes = paint.getShapes();
			for(int i=0;i<shapes.length;i++){
				bw.write("<shape id = "+"\""+shapes[i].getClass().getName()+"\">");
				if(shapes[i].getPosition()!=null) {
					bw.write("<x>"+((Point)shapes[i].getPosition()).x+"</x>");
					bw.write("<y>"+((Point)shapes[i].getPosition()).y+"</y>");
				}else {
					bw.write("<x>"+-1+"</x>");
					bw.write("<y>"+-1+"</y>");
				}
				bw.write("<map>");
				Map<String , Double> map = shapes[i].getProperties();
				if(map!=null) {
					for(Entry<String, Double> e: map.entrySet()) {
						bw.write("<"+e.getKey()+">");
						if(e.getValue()==null) {
							bw.write(-1);
						}else {
							bw.write(e.getValue().toString());
						}
						bw.write("</"+e.getKey()+">");
					}
				}else {
					bw.write("<key>-1</key>");
				}
				bw.write("</map>");
				bw.write("</shape>");
			}
			bw.write("</paint>");
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void loadXML(String path , MyPaint p){
		File file = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("shape");
				for(int i = 0;i<nList.getLength();i++) {
					Node node = nList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
							Class<?> classShape = Class.forName(element.getAttribute("id"));
							Constructor<?> ctor = classShape.getConstructor();
							Shape shape = (Shape) ctor.newInstance(new Object[] { });
							Point position = new Point();
			        		position.x=Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent());
			        		position.y=Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent());
			        		if(position.x==-1&&position.y==-1) {
			        			shape.setPosition(null);
			        		}else {
			        			shape.setPosition(position);
			        		}
			        		Map <String, Double>properties = new HashMap<String, Double>();
			        		NodeList map = element.getElementsByTagName("map").item(0).getChildNodes();
			        		for(int j=0;j<map.getLength();j++) {
			        			Node nodeMap = map.item(j);
			        			if (nodeMap.getNodeType() == Node.ELEMENT_NODE) {
			        				Element elementMap = (Element) nodeMap;
			        				String key = elementMap.getTagName();
			        				Double value = Double.parseDouble(elementMap.getTextContent());
			        				if(!key.equals("key")) {
			        					properties.put(key, value);
			        				}else {
			        					shape.setProperties(null);
			        				}
			        			}
			        		}
			        		shape.setProperties(properties);
			        		p.addShape(shape);
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void WriteJSON(String path , MyPaint paint) {
		
	}
	public static void loadJson(String path , MyPaint p) {
		
	}
}
