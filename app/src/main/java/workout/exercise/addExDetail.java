package workout.exercise;


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

import workout.WorkoutBuilderScreen;
import workout.WorkoutObject;
import workout.volleyRequests.SendExercyseRequest;

public class addExDetail extends AppCompatActivity {

    private EditText reps;
    private EditText sets;
    private EditText wt;
    private Button done;
    private Session session;
    private WorkoutObject currentWkt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        setContentView(R.layout.activity_add_ex_detail);
        currentWkt = getIntent().getExtras().getParcelable("workout");
        System.out.println("made it to point A : " + currentWkt.getName());
        initInput();
    }


    /**
     * Init the texts and button
     */
    private void initInput(){
        sets = (EditText) findViewById(R.id.etSets);
        reps = (EditText) findViewById(R.id.etReps);
        initWt(getIntent().getExtras().getString("baseId"));
        done = (Button) findViewById(R.id.btnDone);
        System.out.println("made it to point B  : " + currentWkt.getId());
        initClick();
    }


    private void initWt(String baseId) {
        int id = Integer.parseInt(baseId);
        wt = (EditText) findViewById(R.id.etWt);
        if (id < 5) {
            wt.setVisibility(View.GONE);
        }
    }

    private int getSets() {
        if (!sets.getText().toString().isEmpty()) {
            return Integer.parseInt(sets.getText().toString());
        } else {
            new AlertDialog.Builder(addExDetail.this)
                    .setMessage("Please enter sets.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            return Integer.parseInt(null);
        }
    }

    private int getReps() {
        if (!reps.getText().toString().isEmpty()) {
            return Integer.parseInt(reps.getText().toString());
        } else {
            new AlertDialog.Builder(addExDetail.this)
                    .setMessage("Please enter reps.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            return Integer.parseInt(null);
        }
    }

    private int getWt() {
        if (!wt.isShown()) {
            return 0;
        } else if (!wt.getText().toString().isEmpty()) {
            return Integer.parseInt(wt.getText().toString());
        } else {
            new AlertDialog.Builder(addExDetail.this)
                    .setMessage("Please enter reps.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            return Integer.parseInt(null);
        }
    }


    /**
     * init the onClick of button
     * adds the ex to the wkt
     * starts the next activity
     */
    private void initClick(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bag = getIntent().getExtras();
                String name = bag.getString("exName");
                String desc = bag.getString("exDes");
                String how = bag.getString("exHow");
                String img = bag.getString("img");
                String base = bag.getString("baseId");
                int numReps = getReps();
                int numSets = getSets();
                int wt = getWt();
                addEx(new ExercyseObject(name, desc, how, img, numReps, numSets, wt, base), currentWkt);
            }
        });
    }


    /**
     * this sends a volley request to add the ex to the wkt
     * add the eq to the wkt and add the ex data to the ex db
     * @param e exercise
     * @param w workout
     */
    private void addEx(ExercyseObject e, WorkoutObject w ){
        final ExercyseObject ex = e;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //this should return the eID of the sent exercise
                try {
                    System.out.println("String response : " + s);
                    //JSONObject json = new JSONObject(s);
                    //String id = json.getString("id");
                    addAndLeave(s, ex);
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }
            }
        };

        try {
            SendExercyseRequest sendEx = new SendExercyseRequest(session.getUsername(), e, w.getId(), "addEx", listener);
            RequestQueue queue = Volley.newRequestQueue(addExDetail.this);
            queue.add(sendEx);
        } catch (NullPointerException f) {
            f.printStackTrace();
        }


    }

    private void addAndLeave(String exId, ExercyseObject e) {
        currentWkt.addEx(exId, e);
        Bundle newBag = new Bundle();
        newBag.putParcelable("workout", currentWkt);
        Intent finish = new Intent(addExDetail.this, WorkoutBuilderScreen.class);
        finish.putExtras(newBag);
        startActivity(finish);
        finish();

    }




}
