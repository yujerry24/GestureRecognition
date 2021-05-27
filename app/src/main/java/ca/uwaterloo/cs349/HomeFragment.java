package ca.uwaterloo.cs349;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragment extends Fragment {

    private SharedViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CanvasRecognize recognizeCanvas = (CanvasRecognize) root.findViewById(R.id.recognize_gesture);
        RecognizeResult resultOne = (RecognizeResult)  root.findViewById(R.id.gesture_result_one);
        RecognizeResult resultTwo = (RecognizeResult) root.findViewById(R.id.gesture_result_two);
        RecognizeResult resultThree = (RecognizeResult) root.findViewById(R.id.gesture_result_three);

        recognizeCanvas.firstMatch = resultOne;
        recognizeCanvas.secondMatch = resultTwo;
        recognizeCanvas.thirdMatch = resultThree;

        recognizeCanvas.gestures = mViewModel.getGestures().getValue();


        return root;
    }



}