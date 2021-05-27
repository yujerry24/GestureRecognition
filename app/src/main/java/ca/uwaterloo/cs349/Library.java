package ca.uwaterloo.cs349;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class Library extends ListView {

    public PathObjectAdapter adapter;
    public SharedViewModel model;

    public Library(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
