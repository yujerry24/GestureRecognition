package ca.uwaterloo.cs349;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class AdditionFragment extends Fragment {

    private SharedViewModel mViewModel;

    private CanvasView myCanvas;
    private String m_Text ="";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_addition, container, false);
        CanvasView canvas = root.findViewById(R.id.canvas);
        myCanvas = canvas;
        Button addButton = (Button) root.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add to path");

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Name your Gesture");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Path addPath = new Path(myCanvas.getPath());
                        mViewModel.addPath(addPath, m_Text);
                    }
                });
                builder.show();

            }
        });

        Button clearButton = (Button) root.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Reset");
                myCanvas.resetPath();
                myCanvas.mPath.reset();
            }
        });
        return root;
    }
}