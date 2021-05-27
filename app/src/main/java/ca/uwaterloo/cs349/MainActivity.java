package ca.uwaterloo.cs349;

import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_addition)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ONRESUME");
        SharedPreferences sharedPreferences = getSharedPreferences("saveFile", MODE_PRIVATE);
        SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        int size = sharedPreferences.getInt("Size", 0);
        viewModel.getGestures().getValue().clear();

        for (int i = 0; i < size; i++) {
            String xPoints = sharedPreferences.getString((i+1)+"x", "");
            String yPoints = sharedPreferences.getString((i+1)+"y", "");
            String [] xTemp = xPoints.split(" ");
            String [] yTemp = yPoints.split(" ");
            PathObject tempObject = new PathObject();
            tempObject.pathName = sharedPreferences.getString((i+1) + "name", "default" + (i+1));
            System.out.println("Name " +tempObject.path);
            Path tempPath = new Path();
            tempPath.moveTo(Float.parseFloat(xTemp[0]), Float.parseFloat(yTemp[0]));
            for (int j = 1; j < 128; j++) {
                tempPath.lineTo(Float.parseFloat(xTemp[j]), Float.parseFloat(yTemp[j]));
            }
            tempObject.path = tempPath;
            tempObject.initValues();
            viewModel.getGestures().getValue().add(tempObject);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferencesTemp = getSharedPreferences("saveFile", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferences = sharedPreferencesTemp.edit();
        SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        ArrayList<PathObject> gestures = viewModel.getGestures().getValue();
        sharedPreferences.putInt("Size", gestures.size()).apply();
        System.out.println("GESTURE SIZE:" + gestures.size());
        for (int i = 0; i < gestures.size(); i++) {
            PathMeasure pm = new PathMeasure(gestures.get(i).path, false);
            float length = pm.getLength();
            float distance = 0f;
            float curr = length/128;
            float []point = new float[2];
            int counter = 0;
            System.out.println("PAUSING");
            String xPoints =  "";
            String yPoints = "";
            while ((counter < 128)) {
                pm.getPosTan(distance, point, null);
                xPoints += point[0] + " ";
                yPoints += point[1] + " ";
                distance += curr;
                counter++;
            }
            xPoints =xPoints.trim();
            yPoints = yPoints.trim();
            System.out.println("PUTTING " + (i+1)+"x" + " : " + xPoints);
            System.out.println("PUTTING " + (i+1)+"y" + " : " + yPoints);
            System.out.println("PUTTING " + (i+1)+"name" + " : " + gestures.get(i).pathName);
            sharedPreferences.putString((i+1)+"x", xPoints).apply();
            sharedPreferences.putString((i+1)+"y", yPoints).apply();
            sharedPreferences.putString((i+1)+"name",gestures.get(i).pathName).apply();

        }



    }

    
}