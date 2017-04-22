package workout.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

import workout.WorkoutObject;
import workout.volleyRequests.GetExercyseRequest;

/**
 * This activity allows the user to select exercyses for further examination and to add them to their workouts
 */
public class ExercyseListScreen extends AppCompatActivity {
    //text
    private TextView wkName;
    //list
    private ListView myListView;
    private ExercyseAdapter adapter;
    //other
    private ArrayList<BaseExercyseObject> baseList;
    private Session session;


    /**
     * Create an activity that lists the base exercyses from the json file
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        setContentView(R.layout.exercyse_list);

        init();
    }

    private void init() {
        wkName = (TextView) findViewById(R.id.tvWkName);
        myListView = (ListView) findViewById(R.id.listEx);

        baseList = new ArrayList<>();
        adapter = new ExercyseAdapter(this, baseList);
        myListView.setAdapter(adapter);
        getBaseList();
        initListClick();
    }

    /**
     * Init the list of exercyses to be displayed and the click listener for that list
     */
    private void initListClick() {
        // Set what happens when a list view item is clicked
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseExercyseObject selectedExercyse = baseList.get(position);
                Intent continueToBuilder = new Intent(ExercyseListScreen.this, addExDetail.class);
                Bundle bag = getIntent().getExtras();
                WorkoutObject w = bag.getParcelable("workout");
                bag.putParcelable("workout", w);
                bag.putString("exName", selectedExercyse.getName());
                bag.putString("exDes", selectedExercyse.getDescription());
                bag.putString("exHow", selectedExercyse.getHowToUrl());
                bag.putString("img", selectedExercyse.getImageString());
                bag.putString("baseId", selectedExercyse.getId());
                continueToBuilder.putExtras(bag);
                startActivity(continueToBuilder);
                finish();
            }
        });
    }

    /**
     * Requests the base list and passes it to the listBuilder function
     */
    private void getBaseList() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray tempList = new JSONArray(s);
                    buildList(tempList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetExercyseRequest getBase = new GetExercyseRequest(session.getUsername(), "", "getBaseList", listener);
        RequestQueue queue = Volley.newRequestQueue(ExercyseListScreen.this);
        queue.add(getBase);
    }


    /**
     * Takes the json array from the volley request and builds it into an arrayList of BaseExercises
     *
     * @param arr JSONArray being passed
     */
    private void buildList(JSONArray arr) {
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject obj = arr.getJSONObject(i);
                System.out.println("THE STRING HAS THIS IN IT:  " + arr.getString(i));
                String name = obj.getString("name");
                String desc = obj.getString("description");
                String img = obj.getString("image");
                String url = obj.getString("howToLink");
                String base = obj.getString("id");
                BaseExercyseObject e = new BaseExercyseObject(name, desc, img, url, base);
                adapter.add(e);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}