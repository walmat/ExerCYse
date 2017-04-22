package workout.action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import workout.WorkoutBuilderScreen;
import workout.WorkoutHome;
import workout.WorkoutObject;
import workout.exercise.BaseExercyseObject;
import workout.exercise.ExercyseAdapter;
import workout.exercise.ExercyseObject;
import workout.volleyRequests.DeleteWorkoutRequest;

/**
 * This is where the user is taken when they select a wkt from the wkt home
 */
public class StartWkt extends AppCompatActivity {
    private Button toStart;
    private Button toEdit;
    private Button btnDelete;
    private Button btnReturn;
    private CheckBox checkBox;
    private TextView wktName;
    private ExercyseAdapter adapter;
    private ListView exList;
    private ArrayList<BaseExercyseObject> exObjs;
    private WorkoutObject currentWorkout;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        Bundle bag = getIntent().getExtras();
        currentWorkout = bag.getParcelable("workout");
        setContentView(R.layout.activity_start_wkt);
        Log.v("WORKOUT NAME : ", currentWorkout.getName());
        initAdapter();
        setButtons();
        wktName = (TextView) findViewById(R.id.textView);
        wktName.setText(currentWorkout.getName());
    }

    private void initAdapter(){
        exObjs = new ArrayList<>();
        adapter = new ExercyseAdapter(this, exObjs);
        exList = (ListView) findViewById(R.id.exList);
        exList.setAdapter(adapter);
        setList();
    }



    /**
     * set up the list of exercises in the wkt for viewing
     */
    private void setList() {
        for (ExercyseObject e : currentWorkout.getExerciseList()
                ) {
            adapter.add(e);
        }
    }

    /**
     * init the buttons
     */
    private void setButtons(){
        wktName = (TextView) findViewById(R.id.textView);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        toEdit = (Button) findViewById(R.id.btnEdit);
        toStart = (Button) findViewById(R.id.btnStart);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnReturn = (Button) findViewById(R.id.btnHome);
        setDeleteClick();
        setEditClick();
        setStartClick();
        setHomeClick();
    }

    /**
     * the home button will take the user back to wkt home
     */
    private void setHomeClick(){
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(StartWkt.this, WorkoutHome.class);
                startActivity(toHome);
                finish();
            }
        });
    }

    /**
     * send the user into running a wkt
     */
    private void setStartClick(){
        toStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWkt = new Intent(StartWkt.this, RunExercyse.class);
                Bundle bag = new Bundle();
                bag.putParcelable("workout", currentWorkout);
                bag.putString("timer", String.valueOf(checkBox.isChecked()));
                startWkt.putExtras(bag);
                session.setIndex("0");
                startActivity(startWkt);
                finish();
            }
        });
    }

    /**
     * send the user to the edit activtiy
     */
    private void setEditClick(){
        toEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEdit = new Intent(StartWkt.this, WorkoutBuilderScreen.class);
                Bundle bag = new Bundle();
                bag.putParcelable("workout", currentWorkout);
                toEdit.putExtras(bag);
                startActivity(toEdit);
                finish();
            }
        });
    }

    /**
     * delete the wkt
     */
    private void setDeleteClick(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make sure to prompt the user in case they press accidentally
                delete();
            }
        });
    }

    private void delete(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent exitScreen = new Intent(StartWkt.this, WorkoutHome.class);
                startActivity(exitScreen);
                finish();
            }
        };
        DeleteWorkoutRequest deleteWorkoutRequest = new DeleteWorkoutRequest(currentWorkout.getId(), listener);
        RequestQueue queue = Volley.newRequestQueue(StartWkt.this);
        queue.add(deleteWorkoutRequest);
    }
}
