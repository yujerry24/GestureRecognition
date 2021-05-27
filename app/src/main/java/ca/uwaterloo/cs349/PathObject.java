package ca.uwaterloo.cs349;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;

import java.util.ArrayList;

public class PathObject {

    public Path path;
    public String pathName;
    public ArrayList<Float> xPoints;
    public ArrayList<Float> yPoints;

    public PathObject() {
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();
    }

    public PathObject(Path p, String str) {
        path = p;
        pathName = str;
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();
    }

    public void initValues() {
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        System.out.println(length);
        int numPoints = 128;


        float distance = 0f;
        float curr = length/128;
        float []point = new float[2];
        float centroidX = 0;
        float centroidY = 0;
        int counter = 0;
        ArrayList<Float> tempXPoints = new ArrayList<Float>();
        ArrayList<Float> tempYPoints = new ArrayList<Float>();
        while ((counter < 128)) {
            pm.getPosTan(distance, point, null);
            tempXPoints.add(point[0]);
            tempYPoints.add(point[1]);
            System.out.print(point[0]+ " ");
            System.out.println(point[1]);
            distance += curr;
            counter++;
            centroidX += point[0];
            centroidY += point[1];
        }

        centroidX /= 128.0;
        centroidY /= 128.0;

        for (int i = 0; i < tempXPoints.size(); i++) {
            tempXPoints.set(i,tempXPoints.get(i)-centroidX);
            tempYPoints.set(i,tempYPoints.get(i)-centroidY);
        }

        System.out.println("New 1st x: " +  tempXPoints.get(0));
        System.out.println("New 1st y: " +  tempYPoints.get(0));


        // Rotate the points
        float angle = (float) Math.toDegrees(Math.atan2(tempYPoints.get(0), tempXPoints.get(0)));
        Matrix m = new Matrix();
        m.setRotate(-angle, 0,0);


        for (int i = 0; i < tempXPoints.size(); i++) {
            float [] coordinate = new float[2];
            coordinate[0] = tempXPoints.get(i);
            coordinate[1] = tempYPoints.get(i);
            m.mapPoints(coordinate);
            tempXPoints.set(i, coordinate[0]);
            tempYPoints.set(i, coordinate[1]);
        }

        System.out.println("First point");
        System.out.println(tempXPoints.get(0));
        System.out.println(tempYPoints.get(0));

        // Rescale the image
        float largestX = 0;
        float largestY = 0;
        float smallestX = 0;
        float smallestY = 0;

        for (int i = 0; i < tempXPoints.size(); i++) {
            if (tempXPoints.get(i) > largestX) {
                largestX = tempXPoints.get(i);
            } else if (tempXPoints.get(i) < smallestX) {
                smallestX = tempXPoints.get(i);
            }

            if (tempYPoints.get(i) > largestY) {
                largestY = tempYPoints.get(i);
            } else if (tempYPoints.get(i) < smallestY) {
                smallestY = tempYPoints.get(i);
            }
        }

        float largestDifference = Math.max(largestX-smallestX, largestY-smallestY);
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(100/largestDifference, 100/largestDifference);
        for (int i = 0; i < tempXPoints.size(); i++) {
            float [] coordinate = new float[2];
            coordinate[0] = tempXPoints.get(i);
            coordinate[1] = tempYPoints.get(i);
            scaleMatrix.mapPoints(coordinate);
            xPoints.add(coordinate[0]);
            yPoints.add(coordinate[1]);
        }

    }
}
