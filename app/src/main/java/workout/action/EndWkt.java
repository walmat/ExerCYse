package workout.action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import workout.WorkoutHome;
import workout.WorkoutObject;
import workout.exercise.BaseExercyseObject;
import workout.exercise.ExercyseObject;

public class EndWkt extends AppCompatActivity {
    private Button btnDone;

    private TextView time;
    private TextView cals;
    private TextView exNum;

    private Bundle bag;
    private WorkoutObject currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_wkt);
        bag = getIntent().getExtras();
        currentWorkout = bag.getParcelable("workout");
        initButton();
        initNums();
    }


    private void initNums(){
        time = (TextView) findViewById(R.id.timeVal);
        cals = (TextView) findViewById(R.id.calVal);
        exNum = (TextView) findViewById(R.id.exs);
        time.setText(currentWorkout.getTime().toString());

        //cals.setText(Integer.toString(currentWorkout.getExercyseIds().length()*100));
        //exNum.setText(currentWorkout.getExercyseIds().length());
    }

    private void initButton(){
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(EndWkt.this, WorkoutHome.class);
                startActivity(goHome);
                finish();
            }
        });
    }

}
