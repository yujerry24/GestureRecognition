package ca.uwaterloo.cs349;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class PathObjectAdapter extends ArrayAdapter<PathObject> {

    public final Context context;
    public final ArrayList<PathObject> gestures;

    public PathObjectAdapter(Context context, ArrayList<PathObject> gestures) {
        super(context, 0, gestures);
        this.context = context;
        this.gestures = gestures;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PathObject gesture = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.path_viewcard, parent, false);
        }

        TextView gestureName = (TextView) convertView.findViewById(R.id.pathName);
        gestureName.setText(gesture.pathName);

        final CanvasView canvas = (CanvasView) convertView.findViewById(R.id.thumbnail);
        canvas.setThumbnailPath(gesture.path);

        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestures.remove(position);
                notifyDataSetChanged();
            }
        });

        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View updateGesture = inflater.inflate(R.layout.fragment_addition, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(updateGesture);

                final AlertDialog editGesture = builder.create();

                final CanvasView canvasTwo = updateGesture.findViewById(R.id.canvas);
                Button editButton = (Button) updateGesture.findViewById(R.id.add_button);
                editButton.setText("Replace");
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PathObject tempGesture = new PathObject(canvasTwo.getPath(), getItem(position).pathName);
                        tempGesture.initValues();
                        gestures.set(position, tempGesture);
                        canvas.setThumbnailPath(canvasTwo.getPath());
                        editGesture.dismiss();
                    }
                });

                Button clearButton = (Button) updateGesture.findViewById(R.id.clear_button);
                clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        canvasTwo.resetPath();
                    }
                });

                editGesture.show();
            }
        });
        notifyDataSetChanged();
        return convertView;
    }
}
