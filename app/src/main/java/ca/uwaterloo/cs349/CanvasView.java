package ca.uwaterloo.cs349;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CanvasView extends View {

    public Path mPath;
    public Paint       mPaint;


    public CanvasView(Context c) {
        super(c);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPath = new Path();
    }

    public CanvasView(Context c, AttributeSet attr) {
        super(c, attr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPath = new Path();
    }

    public CanvasView(Context c, AttributeSet attr, int defStyle) {
        super(c, attr, defStyle);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public Path getPath() {
        return this.mPath;
    }

    public void setThumbnailPath(Path path) {
        Path temp = new Path(path);
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
        m.setTranslate(150-centroidX, 150-centroidY);

        this.mPath = temp;
        mPath.transform(m);

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
        scaleMatrix.setScale(150/largestDifference, 150/largestDifference,150,150);
        mPath.transform(scaleMatrix);
        invalidate();
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
    }
    private void touch_move(float x, float y) {
        mPath.lineTo(x,y);
    }
    private void touch_up() {

    }

    public void resetPath() {
     mPath.reset();
     invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                break;
        }
        invalidate();
        return true;
    }
}


