package workout.action;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import workout.WorkoutObject;
import workout.exercise.ExercyseObject;

public class RunExercyse extends AppCompatActivity {

    private ProgressBar progressBar;
    private CountDownTimer timer;

    private TextView wkName;
    private TextView exName;
    private TextView numReps;
    private TextView numSets;
    private TextView numWt;

    private Button next;
    private Button prev;
    private Button end;

    private WorkoutObject currentWkt;
    private ExercyseObject currEx;
    private int index;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        index = Integer.parseInt(session.getIndex());
        Bundle bag = getIntent().getExtras();
        currentWkt = bag.getParcelable("workout");
        index = Integer.parseInt(session.getIndex());
        currEx = currentWkt.getExerciseList().get(index);
        setContentView(R.layout.activity_run_exercise);
        initButtons();
        setText();
    }

    /**
     * Set up the buttons for next, prev, end
     */
    private void initButtons(){
        next = (Button) findViewById(R.id.btnNext);
        nextClick();
        prev = (Button) findViewById(R.id.btnPrev);
        prevClick();
        end = (Button) findViewById(R.id.btnEnd);
        endClick();
    }

    /**
     * When the user clicks end they are taken to the final summary page
     */
    private void endClick(){
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent endWkt = new Intent(RunExercyse.this, EndWkt.class);
                Bundle bag = new Bundle();
                bag.putParcelable("workout", currentWkt);
                session.setIndex(Integer.toString(index));
                endWkt.putExtras(bag);
                startActivity(endWkt);
                finish();
            }
        });
    }

    /**
     * Take the user back an ex in case the timer moved forward too fast or they hit next on accident
     */
    private void prevClick(){
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index <= 1){
                    alert();
                }
                else{
                    Bundle bag = new Bundle();
                    bag.putParcelable("workout", currentWkt);
                    Intent goBack = new Intent(RunExercyse.this, RunExercyse.class);
                    index--;
                    session.setIndex(Integer.toString(index));
                    startActivity(goBack);
                    finish();
                }
            }
        });
    }

    private void alert() {
        new AlertDialog.Builder(RunExercyse.this)
                .setTitle("")
                .setMessage("Are you sure you want to exit the workout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goBack = new Intent(RunExercyse.this, StartWkt.class);
                        Bundle bag = new Bundle();
                        bag.putParcelable("workout", currentWkt);
                        goBack.putExtras(bag);
                        session.setIndex("0");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.ic_error)
                .show();
    }

    /**
     * Move to the next ex in the wkt or to the summary page when they complete the wkt
     */
    private void nextClick(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent;
                if (index + 1 == currentWkt.getExIds().size()) {
                    nextIntent = new Intent(RunExercyse.this, EndWkt.class);
                } else {
                    nextIntent = new Intent(RunExercyse.this, RunExercyse.class);
                    index++;
                    session.setIndex(Integer.toString(index));
                }
                Bundle bag = new Bundle();
                bag.putParcelable("workout", currentWkt);
                session.setIndex(Integer.toString(index));
                nextIntent.putExtras(bag);
                startActivity(nextIntent);
                finish();
            }
        });

    }

    /**
     * init all the txt
     */
    private void setText(){
        wkName = (TextView) findViewById(R.id.wkName);
        exName = (TextView) findViewById(R.id.exName);
        numReps = (TextView) findViewById(R.id.reps);
        numSets = (TextView) findViewById(R.id.sets);
        numWt = (TextView) findViewById(R.id.weight);
        wkName.setText(currentWkt.getName());
        exName.setText(currEx.getName());
        numReps.setText(Integer.toString(currEx.getNumReps()));
        numSets.setText(Integer.toString(currEx.getNumSets()));
        if (currEx.getWeight() != 0) {
            numWt.setText(Integer.toString(currEx.getWeight()));
        }
    }

}
