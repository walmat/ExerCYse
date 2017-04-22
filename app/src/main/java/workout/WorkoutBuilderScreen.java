package workout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import java.util.ArrayList;

import workout.exercise.BaseExercyseObject;
import workout.exercise.ExercyseAdapter;
import workout.exercise.ExercyseListScreen;
import workout.exercise.ExercyseObject;
import workout.volleyRequests.SendExercyseRequest;
import workout.volleyRequests.SendWorkoutRequest;

/**
 * An activity that allows the user to build workouts and add them to their list of workouts
 */
public class WorkoutBuilderScreen extends AppCompatActivity {

    private WorkoutObject currentWorkout;
    private Session session;
    private ListView exList;
    private ExercyseAdapter adapter;
    private ArrayList<BaseExercyseObject> exObjArr;

    /**
     * Init the WorkoutBuilder activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        setContentView(R.layout.activity_workout_builder);
        Bundle bag = getIntent().getExtras();
        currentWorkout = bag.getParcelable("workout");
        initList();
        initText(currentWorkout.getName());
        setBtnToExChoice();
        setBtnToWkHome();
    }

    /**
     * init the text wktName
     */
    private void initText(String name){
        TextView wkName = (TextView) findViewById(R.id.tvWkName);
        wkName.setText(name);
    }

    /**
     * init the ListView with the adapter
     */
    private void initList(){
        exObjArr = new ArrayList<>();
        adapter = new ExercyseAdapter(this, exObjArr);
        exList = (ListView) findViewById(R.id.listEx);
        exList.setAdapter(adapter);

        try{
            for (ExercyseObject e: currentWorkout.getExerciseList()
                    ) {
                adapter.add(e);
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        //TODO set up onCLick to edit the exercise
    }


    /**
     * Init the button to add exercyses to the current workout
     */
    private void setBtnToExChoice(){
        Button btnToExChoice = (Button) findViewById(R.id.btnAdd);
        btnToExChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExList = new Intent(WorkoutBuilderScreen.this, ExercyseListScreen.class);
                Bundle bag = new Bundle();
                bag.putParcelable("workout", currentWorkout);
                toExList.putExtras(bag);
                startActivity(toExList);
                finish();
            }
        });
    }

    /**
     * Init the button to complete the workout and send it to the server, then take you back to workout home
     */
    private void setBtnToWkHome(){
        Button btnToWkHome = (Button) findViewById(R.id.btnDone);
        btnToWkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toWkHome = new Intent(WorkoutBuilderScreen.this, WorkoutHome.class);
                startActivity(toWkHome);
                finish();
            }
        });
    }

    private void addDesc(){
        Button btnNotes = (Button) findViewById(R.id.btnNotes);
        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO pull up a window to enter notes/desc? up for options yet
            }
        });
    }
}
