package eg.edu.alexu.csd.oop.draw.cs53_cs36.view;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyPaint;
import eg.edu.alexu.csd.oop.draw.cs53_cs36.model.MyShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
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
    private ToggleButton circleButton;
    @FXML
    private ToggleButton moveButton;
    @FXML
    private ToggleButton reSizeButton;
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
	private GraphicsContext workingPicture;
	private Double startX, startY, lastX, lastY;
	private String typeToDo = "Line";
	private ToggleGroup toggleGroupForShapes;
	private MyShape shapeBeingDragged = null;
	private MyShape oldShape = null;
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

	}
	@FXML
	private void onMousePressedListener(MouseEvent e) throws Exception {
		paintEngine.refresh(finalCanvas);
		this.startX = this.lastX = e.getX();
		this.startY= this.lastY = e.getY();
		if(typeToDo.equals("Move")||typeToDo.equals("Resize")) {
			for(int i=paintEngine.getShapes().length-1;i>=0;i--) {
				 oldShape = (MyShape) paintEngine.getShapes()[i];
				if(oldShape.isCountainsPoint(startX.intValue(),startY.intValue())) {
					oldShape.drawBonds(finalCanvas);
					shapeBeingDragged = (MyShape) oldShape.clone();
					System.out.println("Move is detected");
					return;
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
			}
			return;
		}
		this.lastX = e.getX();
		this.lastY = e.getY();
		workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
		Class<?> classShape = Class.forName("eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes."+typeToDo);
		Constructor<?> ctor = classShape.getConstructors()[0];
		Shape shape = (Shape) ctor.newInstance(new Object[] {new Point(startX.intValue(), startY.intValue()),new Point(lastX.intValue(), lastY.intValue()) });
		shape.draw(workingCanvas);
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
			}
			return;
		}
		Class<?> classShape = Class.forName("eg.edu.alexu.csd.oop.draw.cs53_cs36.shapes."+typeToDo);
		Constructor<?> ctor = classShape.getConstructors()[0];
		Shape shape = (Shape) ctor.newInstance(new Object[] {new Point(startX.intValue(), startY.intValue()),new Point(lastX.intValue(), lastY.intValue()) });
		paintEngine.addShape(shape);
		paintEngine.refresh(finalCanvas);
		workingPicture.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
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
	private void clickUndoORredo(ActionEvent e) throws Exception {
		Button b = (Button) e.getSource();
		switch(b.getText()) {
		case"Undo":paintEngine.undo();break;
		case"Redo":paintEngine.redo();break;
		case"Copy":oldShape = (MyShape) shapeBeingDragged.clone();break;
		case"Paste":{Point p = (Point) oldShape.getPosition();
		oldShape.setPosition(new Point(p.x+5, p.y+5));
			paintEngine.addShape(oldShape);
			}break;
		}
		paintEngine.refresh(finalCanvas);		
		return;
	}

}
