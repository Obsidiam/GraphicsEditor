package editor;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;

/**
 * Being the most important class of the project, the Controller class takes care of all the logic behind the GUI.
 * It defines methods used for drawing, resizing and moving, together with saving and loading files.
 */
public class Controller
{
    @FXML private Pane pane;
    private ContextMenu contextMenu;
    private ArrayList<Shape> shapes;
    private Shape activeShape;
    private double initialX, initialY;

    /**
     * Performs certain actions with predefined in the XML file Nodes;
     * Sets up the ContextMenu object, responsible for color changing and deleting a certain object.
     */
    public void initialize()
    {
        shapes = new ArrayList<>();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem setColor = new MenuItem("Set color");
        MenuItem delete = new MenuItem("Delete");
        setColor.setOnAction(event ->
        {
            ColorPicker colorPicker = new ColorPicker();
            colorPicker.setValue((Color)activeShape.getFill());
            pane.getChildren().add(colorPicker);
            colorPicker.setOnAction(event1 ->
            {
                activeShape.setFill(colorPicker.getValue());
                pane.getChildren().remove(colorPicker);
            });
        });
        delete.setOnAction(event -> {
            pane.getChildren().remove(activeShape);
            shapes.remove(activeShape);
        });
        contextMenu.getItems().addAll(setColor, delete);
        this.contextMenu = contextMenu;
    }

    /**
     * Displays information about the editor - the author and the name of the program - in an Alert dialog.
     */
    public void showInfo()
    {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Information");
        info.setHeaderText("About this program");
        info.setContentText("Graphics Editor \nAuthor: Eryk Trzeciakiewicz");
        info.showAndWait();
    }

    /**
     * Provides functionality for drawing a Circle - the user clicks in one point on the drawing Pane, making it
     * the center of the new Circle. Next, they move their mouse away and click another time - the distance between
     * the iniital click and the second click is the radius of the circle.
     */
    public void drawCircle()
    {
        pane.setOnMouseClicked(event ->
        {
            double initialX = event.getX();
            double initialY = event.getY();
            pane.setOnMouseClicked(event1 ->
            {
                double newX = event1.getX();
                double newY = event1.getY();
                double radius = Math.sqrt((newX - initialX)*(newX - initialX)+(newY-initialY)*(newY-initialY));
                Circle circle = new Circle(initialX, initialY, radius);
                addFunctionality(circle);
                pane.getChildren().add(circle);
                shapes.add(circle);
                pane.setOnMouseClicked(null);
            });
        });
    }

    /**
     * Provides functionality for drawing a Rectangle - the user clicks their mouse once, selecting the top left vertex.
     * Then, they click another time, choosing the left bottom corner. The distance in the x direction becomes the
     * width, and the distance in the y direction - the height, of the newly created Rectangle.
     */
    public void drawRectangle()
    {
        pane.setOnMouseClicked(event ->
        {
            double initialX = event.getX();
            double initialY = event.getY();
            pane.setOnMouseClicked(event1 ->
            {
                double newX = event1.getX();
                double newY = event1.getY();
                double width = newX - initialX;
                double height = newY - initialY;
                Rectangle rect = new Rectangle(initialX, initialY, width, height);
                addFunctionality(rect);
                pane.getChildren().add(rect);
                shapes.add(rect);
                pane.setOnMouseClicked(null);
            });
        });
    }

    /**
     * Provides the functionality for drawing a Polygon. The user, by way of clicking, chooses as many vertices
     * of the Polygon as they find necessary and then, by performing a double click, they approve their decision and
     * a new Polygon with selected points is created.
      */
    public void drawPolygon()
    {
        ArrayList<Double> points = new ArrayList<>();
        ArrayList<Circle> vertices = new ArrayList<>();
        pane.setOnMouseClicked(event ->
        {
            Double x = event.getX();
            Double y = event.getY();
            Circle vertex = new Circle(x,y,2);
            vertices.add(vertex);
            points.add(x);
            points.add(y);
            pane.getChildren().add(vertex);
            vertex.setOnMouseClicked(event1 ->
            {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(points);
                addFunctionality(poly);
                pane.getChildren().add(poly);
                shapes.add(poly);
                pane.getChildren().removeAll(vertices);
                points.clear();
                vertices.clear();
                vertex.setOnMouseClicked(null);
                pane.setOnMouseClicked(null);
            });
        });
    }

    /**
     * Clears away all Shape objects present on the drawing Pane.
     */
    public void clear()
    {
        pane.getChildren().removeAll(shapes);
        shapes.clear();
    }

    /**
     * Provides the shape passed as argument with the functionality of displaying a context menu on right click.
     * @param shape Shape which the context menu will be added to.
     */
    public void addContextMenu(Shape shape)
    {
        shape.setOnContextMenuRequested(event ->
        {
            contextMenu.show(shape, event.getScreenX(), event.getScreenY());
            activeShape = shape;
        });

    }

    /**
     * Provides the shape with the functionality of being resized (scaled along the X and the Y axes) and also allows its
     * moving on mouse dragged.
     * @param shape The shape to which the functionality will be applied
     */
    public void enableScalingAndMoving(Shape shape)
    {
        Tooltip coordinates = new Tooltip();
        coordinates.install(shape, coordinates);
        shape.setOnMouseEntered(event -> coordinates.setText((int)event.getSceneX()+ ", " + (int)event.getSceneY() ));
        shape.setOnMousePressed(event ->
        {
            initialX = event.getX();
            initialY = event.getY();
        });
        shape.setOnScroll(event ->
        {
            if(event.isControlDown())
            {
                double scalingFactor = 0.05;
                double scale = (event.getDeltaY() < 0) ? (1 - scalingFactor) : (1 + scalingFactor);
                shape.setScaleX(shape.getScaleX() * scale);
                shape.setScaleY(shape.getScaleY() * scale);
            }
        });

        shape.setOnMouseDragged(event ->
        {
            shape.setTranslateX(shape.getTranslateX() + event.getX() - initialX );
            shape.setTranslateY(shape.getTranslateY() + event.getY() - initialY );
            String text = (int)event.getSceneX() + ", " + (int)event.getSceneY();
            coordinates.setText(text);
        });
    }

    /**
     * performs addContextMenu(Shape shape) and enableScalingAndMoving(Shape shape) on its argument.
     * @param shape
     */
    public void addFunctionality(Shape shape)
    {
        enableScalingAndMoving(shape);
        addContextMenu(shape);
    }

    /**
     * Displays a FileChooser enabling the user to specify the file where the current drawing should be saved.
     * Then, serializes the current Shape objects present in the drawing pane and writes them to the previously specified file.
     */
    public void save()
    {
        Window window = pane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showSaveDialog(window);
        if(file != null)
        {
            try
            {
                for(Shape shape : shapes)
                {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                    String textToWrite = Utility.createFileLine(shape);
                    writer.write(textToWrite);
                    writer.newLine();
                    writer.close();
                }
            }
            catch(IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("File does not exist");
                alert.showAndWait();
            }
        }
    }

    /**
     * Opens a FileChooser enabling the user to specify an existing file from which serialized data will be loaded
     * and the image they describe restored.
     */
    public void load()
    {
        Window window = pane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text Files (.txt) ", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(window);
        if(file != null)
        {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while((line = reader.readLine()) != null)
                {
                    ArrayList<String> parameters = Utility.StringToParameters(line);
                    Shape shape = Utility.createShape(parameters);
                    pane.getChildren().add(shape);
                    shapes.add(shape);
                    addFunctionality(shape);

                }
            }
            catch(IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("File is corrupted");
                alert.showAndWait();
            }
        }
    }
}

