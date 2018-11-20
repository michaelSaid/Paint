package eg.edu.alexu.csd.oop.draw.cs53_cs36.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
							Shape shape = p.createShape(element.getAttribute("id"));
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
	public static void WriteJSON(String path , MyPaint paint) throws Exception {
		Shape[] shapes = paint.getShapes();
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write("{\n");
		bw.write("\"shapes\": [\n");
		for(int i = 0; i < shapes.length; i++){
			writeProperties(shapes[i] ,bw);
			if(i != shapes.length-1){
				bw.write("},\n");
			}
			else{
				bw.write("}\n");
			}
		}
		bw.write("]\n");
		bw.write("}");
		bw.close();
		
	}
	private static void writeProperties(Shape shape ,BufferedWriter bw) throws IOException{
		bw.write("{\n");
		writeKeyValue("Kind",shape.getClass().getName(),bw);
		bw.write(",\n");
		Map<String, Double>properties = shape.getProperties();
		if(properties != null){
			bw.write(",\n");
			bw.write("\"properties\": {\n");
			Set<String> keys = properties.keySet();
			int counter = 0;
			for(String key : keys){
				counter++;
				writeKeyValue(key, "" + properties.get(key),bw);
				if(counter != keys.size()){
					bw.write(",\n");
				}
			}
			bw.write("\n");
			bw.write("}\n");
		}
}
	private static void writeKeyValue(String key,String value ,BufferedWriter bw){
		try {
			bw.write("\"" + key + "\":" + "\"" + value + "\"");
		} catch (IOException e) {
			
			throw new RuntimeException("error");
		}
	}
	public static void loadJson(String path , MyPaint p) throws Exception {
		int counter = 0;		
		BufferedReader br = new BufferedReader(new FileReader(path));
		StringBuilder builder = new StringBuilder();
		String currentLine = new String();
		for(int i = 0; i < 2; i++){
			builder.append(br.readLine());
		}
		if(!builder.toString().equals("nullnull")){
			while(!((currentLine = br.readLine()).equals("]"))){
				currentLine = br.readLine();
				Shape shape = p.createShape(getValue(currentLine));
				if(shape != null){
					String property = br.readLine();
					Map<String,Double> properties = new HashMap<String,Double>();
					property = br.readLine();
					while(true){
						property = br.readLine();
						if((property.equals("}"))) {
							break;
						}
						String key = getKey(property);
						String value = getValue(property);
						properties.put(key, Double.parseDouble(value));
					}
					shape.setProperties(properties);
					p.addShape(shape);
					currentLine = br.readLine();
					if(currentLine==null) {
						break;
					}
				}
				else if(shape == null && counter == 0){
					p.addShape(shape);
					counter = 1;
				}
			}
		}
		br.close();
	}
	private static String getValue(String string){
		String[] splited = string.split(":");
		String value = splited[1];
		if(value.charAt(value.length()-1) == ','){
			value = value.substring(1, value.length()-2);
		}
		else{
			value = value.substring(1, value.length()-1);
		}
		return value;
	}
	
	private static String getKey(String string){
		String[] splited = string.split(":");
		String value = splited[0];
		value = value.substring(1, value.length()-1);
		return value;
	}
}
