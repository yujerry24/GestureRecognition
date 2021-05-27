package ca.uwaterloo.cs349;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class RecognizeResult extends View {

    Paint gesturePaint;
    Paint gestureName;
    Path path;
    String name;

    public RecognizeResult(Context context) {
        super(context);
        init();
    }

    public RecognizeResult(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecognizeResult(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        gesturePaint = new Paint();
        gesturePaint.setAntiAlias(true);
        gesturePaint.setStyle(Paint.Style.STROKE);
        gesturePaint.setStrokeJoin(Paint.Join.ROUND);
        gesturePaint.setStrokeCap(Paint.Cap.ROUND);
        gesturePaint.setColor(Color.BLACK);
        gesturePaint.setStrokeWidth(12);

        gestureName = new Paint();
        gestureName.setTextSize(50);
        gestureName.setColor(Color.BLACK);

        path = new Path();
        name = new String("");
    }

    public void setPath(Path p, String pathName) {
        name = pathName;
        Path temp = new Path(p);
        PathMeasure pm = new PathMeasure(temp, false);
        float length = pm.getLength();
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
        Matrix m = new Matrix();
        m.setTranslate(120-centroidX, 200-centroidY);

        this.path = temp;
        path.transform(m);

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
        scaleMatrix.setScale(150/largestDifference, 150/largestDifference,120,200);
        path.transform(scaleMatrix);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, gesturePaint);
        canvas.drawText("Result", 90, 50, gestureName);
        canvas.drawText(name, 90, 100, gestureName);
    }


}
