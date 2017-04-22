package community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.HomeActivity;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CommunityActivity extends AppCompatActivity {

    private final HashMap<Integer, JSONparser> models = new HashMap<Integer, JSONparser>();
    private LinearLayout scroll;
    private Intent thisIntent;
    private Session session;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new Session(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        queue = Volley.newRequestQueue(CommunityActivity.this);
        thisIntent = getIntent();
        final String username = session.getUsername();

        scroll = (LinearLayout) findViewById(R.id.scroll);


        /*
        Button btnToHome = (Button) findViewById(R.id.btnToHome);
        btnToHome.setText("Home");
        btnToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                CommunityActivity.this.startActivity(intent);
                finish();
            }
        });
*/
        final ToggleButton workoutBtn = (ToggleButton) findViewById(R.id.btnToggleWorkout);
        final ToggleButton musicBtn = (ToggleButton) findViewById(R.id.btnToggleMusic);
        final ToggleButton userBtn = (ToggleButton) findViewById(R.id.btnToggleUser);


        workoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutBtn.setChecked(true);
                musicBtn.setChecked(false);
                userBtn.setChecked(false);
                loadScrollView("workouts");

                //loadPretendScrollView("workout");
            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutBtn.setChecked(false);
                musicBtn.setChecked(true);
                userBtn.setChecked(false);
                loadScrollView("music");
                //loadPretendScrollView("playlist");
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutBtn.setChecked(false);
                musicBtn.setChecked(false);
                userBtn.setChecked(true);
                loadScrollView("users");
                // loadPretendScrollView("userName");
            }
        });
        //loadScrollView("workouts");


        //String name = thisIntent.getStringExtra("name")
        //loadPretendScrollView("workout");

    }

    private void loadPretendScrollView(String type) {
        scroll.removeAllViews();
        for (int i = 0; i < 25; i++) {

            Button ib = new Button(CommunityActivity.this);
            ib.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            ib.getLayoutParams().height = 150;
            //ib.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            ib.setText("Some long " + type + " title xxxxxxxxxxxxxxxx");
            if (ib.getText().length() > 32) {
                ib.setText(ib.getText().subSequence(0, 32) + "...");
            }
            ib.setTextSize(25);

            scroll.addView(ib);
        }
    }

    private void loadScrollView(final String type) {
        //clear old stuff
        models.clear();
        scroll.removeAllViews();
        System.out.println(type + " ///////(CommunityActivity.loadScrollView)");
        //Listen for response from database
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse;
                    //"{'id':'001','uID':'121','name':'Bench Press','eIDs':'{001,002,003,004}'}"
//                    if(type.equals("music")) {
//                        String stub = "[{\"id\":\"1\",\"uID\":\"5\",\"title\":\"Music\",\"text\":\"Some unnecessarily long description about aspects of the playlist that no one cares about.\",\"userPic\":\"null\",\"link\":\"null\"}]";
//                         jsonResponse = new JSONArray(stub);
//                    }else{
                        jsonResponse = new JSONArray(response);
                    //}

                    System.out.println(jsonResponse.toString());

                    //Build buttons into the scrollView
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        //wm makes it easy to access fields of the workout

                        //store the json model
                        JSONparser wm = new JSONparser(jsonResponse.get(i).toString());
                        models.put(i, wm);



                        //depends on whether workout or user
                        if (type.equals("workouts")) {
                            JSONObject obj = new JSONObject(wm.toString());
                            LinearWorkout lw = new LinearWorkout(CommunityActivity.this, obj.toString());
                            lw.setColor(parseColor("red"));
                            scroll.addView(lw.getLayout());
                        } else if (type.equals("users")) {
                            LinearUser lu = new LinearUser(CommunityActivity.this, wm.toString());
                            lu.setColor(parseColor("red"));
                            scroll.addView(lu.getLayout());
                        } else if(type.equals("music")){
                            LinearPlaylist lp = new LinearPlaylist(CommunityActivity.this, wm.toString());
                            lp.setColor(parseColor("red"));
                            scroll.addView(lp.getLayout());
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request = null;
        switch (type) {
            case "workouts":
                request = new AllWorkoutsRequest("getAllWorkouts", responseListener);
                break;
            case "users":
                request = new AllUsersRequest("getUserInfo", responseListener);
                break;
            case "music":
                request = new AllPlaylistRequests("getAllPlaylists", responseListener);
                break;
            default:
                request = new AllWorkoutsRequest("getAllWorkouts", responseListener);
                break;
        }



        queue.add(request);
    }
    /**
     * A method that parses a color integer based on a given string.
     * @param name name of the color to be parsed
     * @return integer value of the name of the color
     */
    private int parseColor(String name){
        String name2 = name.toLowerCase();
        int color = -8712930;
        //integer values for differing color values
        switch(name2){
            case "blue":
                color = -16749658;
                break;
            case "green":
                color = -9202818;
                break;
            case "orange":
                color = -1213126;
                break;
            case "purple":
                color = -8496485;
                break;
            case "yellow":
                color = -8496485;
                break;
            case "pink":
                color = -1001549;
                break;
            case "red":
                color = -8712930;
                break;
        }
        return color;
    }
    private void cancelQueue(){
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}



