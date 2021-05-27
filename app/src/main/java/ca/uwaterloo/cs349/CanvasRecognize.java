package ca.uwaterloo.cs349;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;

public class CanvasRecognize extends View {

    Paint gesturePaint;
    Path drawnPath;
    ArrayList<PathObject> gestures;
    RecognizeResult firstMatch;
    RecognizeResult secondMatch;
    RecognizeResult thirdMatch;

    public CanvasRecognize(Context context) {
        super(context);
        initPaint();
    }

    public CanvasRecognize(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CanvasRecognize(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    public void initPaint() {
        gesturePaint = new Paint();
        gesturePaint.setStyle(Paint.Style.STROKE);
        gesturePaint.setColor(Color.BLUE);
        gesturePaint.setStrokeWidth(12);

        drawnPath = new Path();
    }

    private void touch_move(float x, float y) {
        drawnPath.lineTo(x,y);
    }
    private void touch_up() {
        PathObject original = new PathObject(drawnPath, "compare");
        original.initValues();
        float scores[] = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
        PathObject matches[] = {null, null, null};
        for (int i = 0; i < gestures.size(); i++) {
            float tempScore = 0;
            for (int j = 0; j < gestures.get(i).xPoints.size(); j++) {
                tempScore += Math.sqrt(Math.pow(gestures.get(i).xPoints.get(j)-original.xPoints.get(j),2)+
                        Math.pow(gestures.get(i).yPoints.get(j)-original.yPoints.get(j),2));
            }
            tempScore /= 128.0;
            System.out.println("Score with gesture " + (i+1) + " is: " + tempScore);
            if (scores[0] == Float.MAX_VALUE || scores[1] == Float.MAX_VALUE || scores[2] == Float.MAX_VALUE) {
                for (int k = 0; k < 3; k++) {
                    if (scores[k] == Float.MAX_VALUE) {
                        scores[k] = tempScore;
                        matches[k] = gestures.get(i);
                        break;
                    }
                }
            } else {
                if (tempScore < scores[0]) {
                    scores[0] = tempScore;
                    matches[0] = gestures.get(i);
                } else if (tempScore < scores[1]) {
                    scores[1] = tempScore;
                    matches[1] = gestures.get(i);
                } else if (tempScore < scores[2]) {
                    scores[2] = tempScore;
                    matches[2] = gestures.get(i);
                }
            }
        }
        if (matches[0] != null) {
            System.out.println("HERE HELLO");
            PathMeasure measure = new PathMeasure();
            measure.setPath(matches[0].path, false);
            System.out.println(measure.getLength());
            firstMatch.setPath(matches[0].path, matches[0].pathName);
        }
        if (matches[1] != null) {
            secondMatch.setPath(matches[1].path, matches[1].pathName);
        }
        if (matches[2] != null) {
            thirdMatch.setPath(matches[2].path, matches[2].pathName);
        }
    }
    private void touch_start(float x, float y) {
        drawnPath.reset();
        drawnPath.moveTo(x, y);
    }

    public void resetPath() {
        drawnPath.reset();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(drawnPath, gesturePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPath();
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
