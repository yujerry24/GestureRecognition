package ca.uwaterloo.cs349;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class LibraryFragment extends Fragment {

    private SharedViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_library, container, false);


        final ListView listView = root.findViewById(R.id.list_item);
        System.out.println(mViewModel.getGestures().getValue().size());
        final PathObjectAdapter adapter = new PathObjectAdapter(this.getContext(), mViewModel.getGestures().getValue());
        listView.setAdapter(adapter);

        return root;
    }
}