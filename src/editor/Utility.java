package editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * The Utility class provides the API with a bunch of auxiliary methods, making the body of the Controller class
 * less elongated.
 */
public class Utility
{
    /**
     * Checks if the argument is an instance of Circle
     * @param shape
     * @return true, if the object is an instance of Circle, false otherwise.
     */
    public static boolean isCircle(Shape shape)
    {
        return shape instanceof Circle;
    }

    /**
     * Checks if the Shape is an instance of Rectangle
     * @param shape
     * @return true, if the object is an instance of Rectangle, false otherwise.
     */
    public static boolean isRectangle(Shape shape)
    {
        return shape instanceof Rectangle;
    }

    /**
     * Serializes the shape
     * @param shape Shape to be serialized
     * @return a String containing the serialized Shape
     */
    public static String createFileLine(Shape shape)
    {
        if(isCircle(shape))
        {
            Circle circle = (Circle) shape;
            return ("Circle" + " " + circle.getCenterX() + " " + circle.getCenterY() + " " + circle.getTranslateX() +" "+circle.getTranslateY() + " " + circle.getScaleX() + " " + circle.getScaleY() + " " + circle.getRadius() + " " + circle.getFill());
        }
        else if(isRectangle(shape))
        {
            Rectangle rectangle = (Rectangle) shape;
            return ("Rectangle" + " " + rectangle.getX() + " " + rectangle.getY() + " " + rectangle.getTranslateX() +" "+rectangle.getTranslateY() + " " + rectangle.getScaleX() + " " + rectangle.getScaleY() + " " + rectangle.getWidth() + " " + rectangle.getHeight() + " " + rectangle.getFill());
        }
        else
        {
            Polygon polygon = (Polygon) shape;
            ObservableList<Double> points = polygon.getPoints();
            String text = "Polygon" + " " + polygon.getTranslateX() + " " + polygon.getTranslateY() + " " + polygon.getScaleX() + " " + polygon.getScaleY();
            for(Double point: points)
            {
                text = text + " "+ point;
            }
            text = text + " " + polygon.getFill();
            return text;
        }
    }

    /**
     * Transforms a serialized line of text into an ArrayList containing all the parameters
     * @param string serialized line
     * @return ArrayList containing the parameters used to serialize the shape.
     */
    public static ArrayList<String> StringToParameters(String string)
    {
        ArrayList<String> parameters = new ArrayList<>();
        String[] parametersArray = string.split(" ");
        for(String s : parametersArray)
        {
            parameters.add(s);
        }
        return parameters;
    }

    /**
     * Generates a Shape object based on the ArrayList of serializing parameters.
     * @param parameters An ArrayList containing the parameters used to serialize the Shape
     * @return Shape creates using parameters used as argument
     */
    public static Shape createShape(ArrayList<String> parameters)
    {
        String shapeType = parameters.get(0);

        if(shapeType.equals("Circle"))
        {
            double centerX = Double.parseDouble(parameters.get(1));
            double centerY = Double.parseDouble(parameters.get(2));
            double translateX = Double.parseDouble(parameters.get(3));
            double translateY = Double.parseDouble(parameters.get(4));
            double scaleX = Double.parseDouble(parameters.get(5));
            double scaleY = Double.parseDouble(parameters.get(6));
            double radius = Double.parseDouble(parameters.get(7));
            Color fill = Color.web(parameters.get(8));
            Circle circle = new Circle(centerX, centerY, radius, fill);
            circle.setTranslateX(translateX);
            circle.setTranslateY(translateY);
            circle.setScaleX(scaleX);
            circle.setScaleY(scaleY);
            return circle;
        }
        else if(shapeType.equals("Rectangle"))
        {
            double posX = Double.parseDouble(parameters.get(1));
            double posY = Double.parseDouble(parameters.get(2));
            double translateX = Double.parseDouble(parameters.get(3));
            double translateY = Double.parseDouble(parameters.get(4));
            double scaleX = Double.parseDouble(parameters.get(5));
            double scaleY = Double.parseDouble(parameters.get(6));
            double width = Double.parseDouble(parameters.get(7));
            double height = Double.parseDouble(parameters.get(8));
            Color fill = Color.web(parameters.get(9));
            Rectangle rectangle = new Rectangle(posX, posY, width, height);
            rectangle.setFill(fill);
            rectangle.setTranslateX(translateX);
            rectangle.setTranslateY(translateY);
            rectangle.setScaleX(scaleX);
            rectangle.setScaleY(scaleY);
            return rectangle;

        }
        else if(shapeType.equals("Polygon"))
        {
            ArrayList<Double> points = new ArrayList<>();
            Color fill = Color.web(parameters.get(parameters.size()-1));
            double translateX = Double.parseDouble(parameters.get(1));
            double translateY = Double.parseDouble(parameters.get(2));
            double scaleX = Double.parseDouble(parameters.get(3));
            double scaleY = Double.parseDouble(parameters.get(4));

            for(int i = 5; i <= parameters.size() - 2; i++)
            {
                Double point = Double.parseDouble(parameters.get(i));
                points.add(point);
            }
            Polygon polygon = new Polygon();
            ObservableList<Double> finalPoints = FXCollections.observableArrayList(points);
            polygon.getPoints().addAll(finalPoints);
            polygon.setFill(fill);
            polygon.setTranslateX(translateX);
            polygon.setTranslateY(translateY);
            polygon.setScaleX(scaleX);
            polygon.setScaleY(scaleY);

            return polygon;
        }
        else
        {
            throw new IllegalArgumentException("File Contains Errors");
        }
    }

}
