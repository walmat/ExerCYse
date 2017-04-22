package workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import org.json.JSONException;
import org.json.JSONObject;

import workout.volleyRequests.GetWorkoutsRequest;

public class StartWorkoutBuild extends AppCompatActivity {

    private EditText wkName;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        setContentView(R.layout.activity_start_workout_build);
        wkName = (EditText) findViewById(R.id.etWkName);
        initButton();
    }

    private String getName() {
        if (!wkName.getText().toString().isEmpty()) {
            return wkName.getText().toString();
        } else {
            new AlertDialog.Builder(StartWorkoutBuild.this)
                    .setMessage("Please enter a name")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            return null;
        }
    }

    private void initButton(){
        Button startBuilding = (Button) findViewById(R.id.btnDone);
        startBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(getName());
                session.setWktName(getName());
                initWorkout();
            }
        });
    }

    private void initWorkout(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    //System.out.println(s);
                    JSONObject json = new JSONObject(s);
                    String id = json.getString("id");
                    WorkoutObject w = new WorkoutObject(session.getWktName(), id);
                    Intent startBuilding = new Intent(StartWorkoutBuild.this, WorkoutBuilderScreen.class);
                    Bundle bag = new Bundle();
                    bag.putParcelable("workout", w);
                    startBuilding.putExtras(bag);
                    startActivity(startBuilding);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        GetWorkoutsRequest handshake = new GetWorkoutsRequest(session.getUsername(), session.getWktName(), "insertWorkout",listener);
        RequestQueue queue = Volley.newRequestQueue(StartWorkoutBuild.this);
        queue.add(handshake);
    }

}
