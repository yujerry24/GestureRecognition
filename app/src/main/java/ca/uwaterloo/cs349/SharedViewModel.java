package ca.uwaterloo.cs349;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public MutableLiveData<ArrayList<PathObject>> pathObjects;


    public SharedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is shared model");
        System.out.println("test");
        pathObjects = new MutableLiveData<>();
        ArrayList<PathObject> temp = new ArrayList<>();
        pathObjects.setValue(temp);

    }

    public LiveData<ArrayList<PathObject>> getGestures() {
        return this.pathObjects;
    }

    public void addPath(Path path, String gestureName) {
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        System.out.println(length);
        System.out.println(gestureName);
        int numPoints = 128;
        PathObject temp = new PathObject();
        temp.path = path;
        temp.xPoints = new ArrayList<Float>();
        temp.yPoints = new ArrayList<Float>();

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
            temp.xPoints.add(coordinate[0]);
            temp.yPoints.add(coordinate[1]);
        }

        temp.pathName = gestureName;
        Objects.requireNonNull(this.pathObjects.getValue()).add(temp);

    }

    public LiveData<String> getText() {
        return mText;
    }

}