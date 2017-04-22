package workout;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import workout.action.StartWkt;
import workout.exercise.ExercyseObject;
import workout.volleyRequests.GetWorkoutsRequest;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class WorkoutHome extends AppCompatActivity {

    TextView tvUsername;
    Session session;
    ListView workoutsList;
    ArrayList<WorkoutObject> workoutObjects;
    WorkoutAdapter adapter;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        setContentView(R.layout.activity_workout_home);
        tvUsername = (TextView) findViewById(R.id.tvUsr);
        tvUsername.setText(session.getUsername());
        setUp();
        getUserWorkouts();
        setListClick();
    }

    private void getUserWorkouts(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    JSONArray response = new JSONArray(s);
                    for(int i = 0; i < response.length(); i++){
                        JSONObject temp = response.getJSONObject(i);
                        //System.out.println("obj " + i + " " + temp.toString());
                        String id = temp.getString("id");
                        String name = temp.getString("name");
                        String exData = temp.getString("exData");
                        JSONArray exDataArr = new JSONArray(exData);
                        ArrayList<String> strList = new ArrayList<>();
                        ArrayList<ExercyseObject> exObjList = new ArrayList<>();
                        for (int j = 0; j < exDataArr.length(); j++){
                            JSONObject tempEx = exDataArr.getJSONObject(j);
                            ExercyseObject e = buildEx(tempEx);
                            if(e != null){
                                String exId = tempEx.getString("id");
                                strList.add(exId);
                                exObjList.add(e);
                            }
                        }
                        WorkoutObject w = new WorkoutObject(name, id, exObjList, strList);
                        adapter.add(w);
                    }
                } catch (JSONException | NumberFormatException e) {
                    //e.printStackTrace();
                }
            }
        };
        GetWorkoutsRequest getWorkoutsRequest = new GetWorkoutsRequest(session.getUsername(), "", "getFullWktForUser", listener);
        RequestQueue queue = Volley.newRequestQueue(WorkoutHome.this);
        queue.add(getWorkoutsRequest);
        //System.out.println("request sent");
    }

    @Nullable
    private ExercyseObject buildEx(JSONObject obj){
        try {
            String baseId = obj.getString("baseId");
            String desc = obj.getString("desc");
            String img = obj.getString("img");
            String url = obj.getString("url");
            String exName = obj.getString("name");
            int sets = Integer.parseInt(obj.getString("sets"));
            int reps = Integer.parseInt(obj.getString("reps"));
            int wt = Integer.parseInt(obj.getString("wt"));
            ExercyseObject e = new ExercyseObject(exName, desc, url, img, reps, sets, wt, baseId);
            return e;
        }catch(JSONException |  NumberFormatException e){
            e.printStackTrace();
        }
        return null;
    }

    private void setUp(){
        workoutObjects = new ArrayList<>();
        adapter = new WorkoutAdapter(this, workoutObjects);
        workoutsList = (ListView) findViewById(R.id.listWk);
        workoutsList.setAdapter(adapter);
        setListClick();
        btnAdd = (Button) findViewById(R.id.btnAdd);
        setBtnClick();
    }

    private void setBtnClick(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toNewWktIntent = new Intent(WorkoutHome.this, StartWorkoutBuild.class);
                startActivity(toNewWktIntent);
                finish();
            }
        });
    }

    private void setListClick(){
        workoutsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkoutObject selectedWkt = workoutObjects.get(position);
                Intent startWktIntent = new Intent(WorkoutHome.this, StartWkt.class);
                Bundle bag = new Bundle();
                bag.putParcelable("workout", selectedWkt);
                startWktIntent.putExtras(bag);
                startActivity(startWktIntent);
                finish();
            }
        });

    }
}