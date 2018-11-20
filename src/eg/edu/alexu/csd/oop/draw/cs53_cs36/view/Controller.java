package eg.edu.alexu.csd.oop.draw.cs53_cs36.view;

import java.awt.Desktop;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyPaint;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyShape;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class Controller implements Initializable {

	@FXML
	Canvas workingCanvas;
	@FXML
	Canvas finalCanvas;
    @FXML
    private ToggleButton ellipseButton;
    @FXML
    private ToggleButton rectangleButton;
    @FXML
    private ToggleButton lineButton;
    @FXML
    private ToggleButton squareButton;
    @FXML
    private ToggleButton triangleButton;
    @FXML
    private ToggleButton roundButton;
    @FXML
    private ToggleButton circleButton;
    @FXML
    private ToggleButton moveButton;
    @FXML
    private ToggleButton reSizeButton;
    @FXML
    private ToggleButton selectButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button pasteButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ColorPicker color;
    @FXML
    private ColorPicker colorframe;
    @FXML 
    private Slider strokeSlider;
	private GraphicsContext workingPicture;
	private Double startX, startY, lastX, lastY;
	private String typeToDo = "";
	private ToggleGroup toggleGroupForShapes;
	private MyShape shapeBeingDragged = null;
	private MyShape oldShape = null;
	private MyShape selectedShape = null;
	private Shape oldRound = null;
	private Shape round = null;
	private Stage stage;
	MyPaint paintEngine;	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			paintEngine = new MyPaint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workingPicture = workingCanvas.getGraphicsContext2D();
		toggleGroupForShapes = new ToggleGroup();
		rectangleButton.setToggleGroup(toggleGroupForShapes);
		lineButton.setToggleGroup(toggleGroupForShapes);
		squareButton.setToggleGroup(toggleGroupForShapes);
		triangleButton.setToggleGroup(toggleGroupForShapes);
		circleButton.setToggleGroup(toggleGroupForShapes);
		ellipseButton.setToggleGroup(toggleGroupForShapes);
		moveButton.setToggleGroup(toggleGroupForShapes);
		reSizeButton.setToggleGroup(toggleGroupForShapes);
		selectButton.setToggleGroup(toggleGroupForShapes);
		roundButton.setToggleGroup(toggleGroupForShapes);
		strokeSlider.setMin(1);
		strokeSlider.setMax(15);
		colorframe.setValue(Color.BLACK);
		color.setValue(Color.WHITE);
	}
	@FXML
	private void onMousePressedListener(MouseEvent e) throws Exception {
		paintEngine.refresh(finalCanvas);
		this.startX = this.lastX = e.getX();
		this.startY= this.lastY = e.getY();
		if(typeToDo.equals("Move")||typeToDo.equals("Resize")||typeToDo.equals("Select")) {
			for(int i=paintEngine.getShapes().length-1;i>=0;i--) {
				
			if(!paintEngine.getShapes()[i].getClass().getName().equals("eg.edu.alexu.csd.oop.draw.RoundRectangle")) {
				 oldShape = (MyShape) paintEngine.getShapes()[i];
				if(oldShape.isCountainsPoint(startX.intValue(),startY.intValue())) {
					selectedShape=(MyShape) oldShape.clone();
					oldShape.drawBonds(finalCanvas);
					shapeBeingDragged = (MyShape) oldShape.clone();
					System.out.println("Move is detected");
					return;
				}
			}else {
				oldRound = paintEngine.getShapes()[i];
				if(roundContains(startX, startY ,oldRound)) {
					round =  (Shape) oldRound.clone();
					System.out.println("round is detected");
					return;
					}
				}
			}
			return;
		}
	}
	@FXML
	private void onMouseDraggedListener(MouseEvent e) throws Exception {
		if(typeToDo.equals("Move")) {
			this.lastX = e.getX();
			this.lastY = e.getY();
			if(shapeBeingDragged!=null) {
				shapeBeingDragged.moveBy(lastX.intValue()-startX.intValue(), lastY.intValue()-startY.intValue());
				this.startX = this.lastX;
				this.startY = this.lastY;
				workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
				shapeBeingDragged.drawBonds(workingCanvas);
				shapeBeingDragged.draw(workingCanvas);
			}else if(round!=null) {
				roundMoveBy(lastX.intValue()-startX.intValue(), lastY.intValue()-startY.intValue() ,round);
				this.startX = this.lastX;
				this.startY = this.lastY;
				workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
				paintEngine.drawRounRect(workingCanvas, round);
			}
			return;
		}
		if(typeToDo.equals("Resize")) {
			this.lastX = e.getX();
			this.lastY = e.getY();
			if(shapeBeingDragged!=null) {
				shapeBeingDragged.reSizeBy(lastX.intValue()-startX.intValue(), lastY.intValue()-startY.intValue());
				this.startX = this.lastX;
				this.startY = this.lastY;
				workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
				shapeBeingDragged.drawBonds(workingCanvas);
				shapeBeingDragged.draw(workingCanvas);
			}else if(round!=null) {
				roundResizeBy(lastX.intValue()-startX.intValue(), lastY.intValue()-startY.intValue(),round);
				this.startX = this.lastX;
				this.startY = this.lastY;
				workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
				paintEngine.drawRounRect(workingCanvas, round);
			}
			return;
		}
		if(typeToDo.equals("Select")||typeToDo.isEmpty())
			return;
		this.lastX = e.getX();
		this.lastY = e.getY();
		workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
		String nameClass = "eg.edu.alexu.csd.oop.draw.";
		if(!typeToDo.equals("RoundRectangle")) {
			nameClass+="cs53_cs36.shapes.";
		}
		Class<? extends Shape> classShape = paintEngine.createClass(nameClass+typeToDo);
		if(classShape!=null) {
		Constructor<?> ctor = classShape.getConstructors()[0];
		if(!typeToDo.equals("RoundRectangle")) {
			MyShape shape = (MyShape) ctor.newInstance(new Object[] {new Point(startX.intValue(), startY.intValue()),new Point(lastX.intValue(), lastY.intValue()) });
			shape.getProperties().put("stroke",strokeSlider.getValue());
			shape.setColor(colorframe.getValue());
			shape.setFillColor(color.getValue());
			shape.draw(workingCanvas);
		}else {
			Double x1 = Math.min(startX, lastX);
			Double y1 = Math.min(startY, lastY);
			double height = Math.abs(startY - lastY);
			double width = Math.abs(startX - lastX);
			workingPicture.setLineWidth(strokeSlider.getValue());
			workingPicture.setFill(color.getValue());
			workingPicture.setStroke(colorframe.getValue());
			workingPicture.fillRoundRect(x1, y1, width, height, 40,40);
			workingPicture.strokeRoundRect(x1, y1, width, height, 40,40);
		}

	}
}
	@FXML
	private void onMouseReleaseListener(MouseEvent e) throws Exception {
		if(typeToDo.equals("Move")||typeToDo.equals("Resize")) {
			if(shapeBeingDragged!=null&&oldShape!=null) {
			paintEngine.updateShape(oldShape, shapeBeingDragged);
			paintEngine.refresh(finalCanvas);
			shapeBeingDragged.drawBonds(finalCanvas);
			shapeBeingDragged = null;
			oldShape=null;
			workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
			}else if(round!=null&&oldRound!=null) {
				paintEngine.updateShape(oldRound, round);
				paintEngine.refresh(finalCanvas);
				round= null;
				oldRound=null;
				workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
			}
			return;
		}
		if(typeToDo.equals("Select")||typeToDo.isEmpty())
			return;
		String nameClass = "eg.edu.alexu.csd.oop.draw.";
		if(!typeToDo.equals("RoundRectangle")) {
			nameClass+="cs53_cs36.shapes.";
		}
		Class<? extends Shape> classShape = paintEngine.createClass(nameClass+typeToDo);
		if(classShape!=null) {
		Constructor<?> ctor = classShape.getConstructors()[0];
		if(!typeToDo.equals("RoundRectangle")) {
			MyShape shape = (MyShape) ctor.newInstance(new Object[] {new Point(startX.intValue(), startY.intValue()),new Point(lastX.intValue(), lastY.intValue()) });
			shape.getProperties().put("stroke",strokeSlider.getValue());
			shape.setColor(colorframe.getValue());
			shape.setFillColor(color.getValue());
			paintEngine.addShape(shape);
			paintEngine.refresh(finalCanvas);
			workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
		}else {
			Shape	shape = (Shape) ctor.newInstance(new Object[]{});
			Double x1 = Math.min(startX, lastX);
			Double y1 = Math.min(startY, lastY);
			double height = Math.abs(startY - lastY);
			double width = Math.abs(startX - lastX);
			Map<String,Double>map = new HashMap<>();
			map.put("Length", height);
			map.put("Width", width);
			map.put("ArcLength", 40.0);
			map.put("ArcWidth", 40.0);
			map.put("stroke",strokeSlider.getValue());
			shape.setProperties(map);
			System.out.println(shape.getProperties());
			shape.setPosition(new Point(x1.intValue(),y1.intValue()));
			java.awt.Color awtColor = new java.awt.Color(MyShape.getRGB(colorframe.getValue()));
			shape.setColor(awtColor);
			awtColor = new java.awt.Color(MyShape.getRGB(color.getValue()));
			shape.setFillColor(awtColor);
			paintEngine.addShape(shape);
			paintEngine.refresh(finalCanvas);
			workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
		}
		
	}
}
	@FXML
	private void clickButtonShapes(ActionEvent e) {
		paintEngine.refresh(finalCanvas);
		ToggleButton tb = (ToggleButton) e.getSource();
		System.out.println(tb.getId());
		typeToDo = tb.getText();
		System.out.println(typeToDo);
	}
	@FXML
	private void clickButtons(ActionEvent e) throws Exception {
		Button b = (Button) e.getSource();
		switch(b.getText()) {
		case"Undo":paintEngine.undo();break;
		case"Redo":paintEngine.redo();break;
		case"Copy":copy();break;
		case"Paste":paste();break;
		case"Delete":Delete();break;
		default:
			break;
		}
		workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
		paintEngine.refresh(finalCanvas);		
		return;
	}
	@FXML
	private void ClickKeys(KeyEvent e) {
		
	 if(e.isShortcutDown()) {
		switch(e.getCode()) {
		case C:copy();break;
		case V:paste();break;
		case S:save();break;
		case O:load();break;
		case N:New();break;
		case Z:paintEngine.undo();break;
		case Y:paintEngine.redo();break;
		default:
			break;
		}
	}else {
		if(e.getCode()==KeyCode.DELETE)
			Delete();
	}
		workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
		paintEngine.refresh(finalCanvas);		
		return;
	}
	private void Delete(){
		if(selectedShape!=null) {
		paintEngine.removeShape((Shape)selectedShape);
		}else if(round!=null) {
			paintEngine.removeShape(round);
		}
		selectButton.setSelected(false);
		if(typeToDo.equals("Select"))
		typeToDo = "";	
	}
	private void copy() {
		if(shapeBeingDragged!=null) {
		try {
		selectedShape = (MyShape) shapeBeingDragged.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else if(round!=null) {
		try {
			oldRound = (Shape) round.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		selectButton.setSelected(false);
		if(typeToDo.equals("Select"))
		typeToDo = "";
	}
	private void paste() {
		if(selectedShape!=null) {
		selectedShape.moveBy(20, 20);
		try {
			paintEngine.addShape((Shape) selectedShape.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else if(oldRound!=null) {
		roundMoveBy(20, 20, oldRound);
		try {
			paintEngine.addShape((Shape) oldRound.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		selectButton.setSelected(false);
		if(typeToDo.equals("Select"))
		typeToDo = "";
	}
	
	public void init(Stage stage) {
		// TODO Auto-generated method stub
		this.stage=stage;
	}
	@FXML
	private void save() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );
		
		fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("Xml", "*.xml"),
	                new FileChooser.ExtensionFilter("Json", "*.json")
	            );
		File file = fileChooser.showSaveDialog(stage);
		if(file!=null) {
        	paintEngine.save(file.getPath());
        }
	}
	@FXML
	private void load() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("JSON", "*.json")
            );
        File file = fileChooser.showOpenDialog(null);
        if(file!=null) {
        	paintEngine.load(file.getPath());
        	paintEngine.refresh(finalCanvas);
        }
	}
	@FXML
	public void Quit(){
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Paint");
	alert.setHeaderText("Do you want to save the changes");
	alert.setContentText("Choose your option.");
	ButtonType saveButton = new ButtonType("Save");
	ButtonType dSaveButton = new ButtonType("Don't Save");
	ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	alert.getButtonTypes().setAll(saveButton, dSaveButton,buttonTypeCancel);
	Optional<ButtonType> result = alert.showAndWait();
	if (result.get() == saveButton){
	    save();
	}else if (result.get() == dSaveButton) {
	    Platform.exit();
		}
	}
	@FXML
	private void New(){

	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Paint");
	alert.setHeaderText("Do you want to save the changes");
	alert.setContentText("Choose your option.");
	ButtonType saveButton = new ButtonType("Save");
	ButtonType dSaveButton = new ButtonType("Don't Save");
	ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	alert.getButtonTypes().setAll(saveButton, dSaveButton,buttonTypeCancel);
	Optional<ButtonType> result = alert.showAndWait();
	if (result.get() == saveButton){
	    save();
		}
	try {
		paintEngine = new MyPaint();
		paintEngine.refresh(finalCanvas);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	@FXML
	private void snapShot() throws Exception {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		Image snapshot = finalCanvas.snapshot(params, null);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
        File file = fileChooser.showSaveDialog(null);
        if(file!=null) {
        	ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
        }
    }
	@FXML
	private void Colorfill(ActionEvent event){
		Color selectedColor = color.getValue();
		System.out.println(selectedColor);
		if(selectedShape!=null&&oldShape!=null) {
			selectedShape.setFillColor((Object)selectedColor);
			paintEngine.updateShape(oldShape, selectedShape);
			System.out.println(selectedShape.getFillColor());
			}else if(round!=null&&oldRound!=null) {
				java.awt.Color awtColor = new java.awt.Color(MyShape.getRGB(selectedColor));
				round.setFillColor(awtColor);
				paintEngine.updateShape(oldRound, round);
			}
		paintEngine.refresh(finalCanvas);
			selectButton.setSelected(false);
			if(typeToDo.equals("Select"))
				typeToDo = "";	
	}
	@FXML
	private void Colorframe(ActionEvent event){
		Color selectedColor = colorframe.getValue();
		System.out.println(selectedColor);
		if(selectedShape!=null&&oldShape!=null) {
			selectedShape.setColor((Object)selectedColor);
			paintEngine.updateShape(oldShape, selectedShape);
			System.out.println(selectedShape.getColor());
			}else if(round!=null&&oldRound!=null) {
				java.awt.Color awtColor = new java.awt.Color(MyShape.getRGB(selectedColor));
				round.setColor(awtColor);
				paintEngine.updateShape(oldRound, round);
			}
		paintEngine.refresh(finalCanvas);
		
			selectButton.setSelected(false);
			if(typeToDo.equals("Select"))
				typeToDo = "";	
	}
	@FXML void Stroke() {
		double strokeValue = strokeSlider.getValue();
		if(selectedShape!=null&&oldShape!=null) {
			selectedShape.getProperties().put("stroke" ,strokeValue);
			paintEngine.updateShape(oldShape, selectedShape);
		}else if(round!=null&&oldRound!=null) {
			round.getProperties().put("stroke" ,strokeValue);
			paintEngine.updateShape(oldRound, round);
		}
		paintEngine.refresh(finalCanvas);
		selectButton.setSelected(false);
		if(typeToDo.equals("Select"))
			typeToDo = "";	
	}
	private boolean roundContains(double x , double y , Shape r) {
		double width = r.getProperties().get("Width");
		double height=r.getProperties().get("Length");
		return x>=((Point)r.getPosition()).x&&y>=((Point)r.getPosition()).y
				&&x<((Point)r.getPosition()).x+width&&y<((Point)r.getPosition()).y+height;
	}
	private void roundMoveBy(int x , int y , Shape r) {
		r.setPosition(new Point(((Point)r.getPosition()).x+x,((Point)r.getPosition()).y+y));
	}
	private void roundResizeBy(int x , int y , Shape r) {
		double width = r.getProperties().get("Width");
		double height=r.getProperties().get("Length");
		width = Math.abs(width+=x);
		height =Math.abs(height+=y);
		r.getProperties().put("Length", height);
		r.getProperties().put("Width", width);
	}
	@FXML
	private void help() throws Exception{
        File myFile = new File("User.pdf");
        Desktop.getDesktop().open(myFile);
	}
}
