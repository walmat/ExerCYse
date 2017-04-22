package community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

/**
 * Created by Nate on 3/19/2017.
 */

public class CommunityWOActivity extends AppCompatActivity {
    JSONparser workout;
    JSONObject user;
    LinearLayout scroll;
    JSONArray baseList;
    boolean commentTab = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_view);

        //get and fill necessary information
        String jsonGuy = this.getIntent().getStringExtra("model");
        workout = new JSONparser(jsonGuy);
        getBaseList();
        getUserInfo(workout.get("uID"));


        //tab switch between page contents and comments
        final ImageView tab = (ImageView) findViewById(R.id.bannerBreak);
        tab.setClickable(true);
        View.OnClickListener switcher = new View.OnClickListener() {
            public void onClick(View v) {
                switchTab();
            }
        };
        tab.setOnClickListener(switcher);
    }

    /**
     * Gets user info and creates a JSONObject
     * @param id user id
     */
    private void getUserInfo(String id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    user = new JSONObject(response);

                    //use info to load views
                    loadViews();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request = new AllUsersRequest("getUserProfile", responseListener);
        ((AllUsersRequest)request).addParam("user_info", id);

        RequestQueue queue = Volley.newRequestQueue(CommunityWOActivity.this);
        queue.add(request);
    }

    /**
     * Loads the static page elements that do not change with user and workout infor
     */
    private void loadViews() {

        //create instances
        scroll = (LinearLayout) findViewById(R.id.scroll);
        TextView title = (TextView) findViewById(R.id.title);
        TextView author = (TextView) findViewById(R.id.author);
        LinearLayout workBG = (LinearLayout) findViewById(R.id.workBG);


        try {
            //fill profile picture
            ImageView profPic = (ImageView) findViewById(R.id.profPic);
            PicFetcher p = new PicFetcher(profPic, user.getString("username"));
            p.start();

            //fill credits for post
            title.setText(workout.get("name"));
            author.setText("By " + user.get("firstname") + " " + user.get("lastname"));
            workBG.setBackgroundColor(parseColor(user.get("color").toString()));

            //create clickables to change pages
            author.setClickable(true);
            System.out.println(workout.toString()+"\n"+user.toString()+"\n\n\n\n");

            //go to creator's user page
            View.OnClickListener toWOview = new View.OnClickListener() {
                public void onClick(View v) {
                    Intent toView = new Intent(CommunityWOActivity.this, UserViewActivity.class);
                    toView.putExtra("model",user.toString());
                    CommunityWOActivity.this.startActivity(toView);
                }
            };
            author.setOnClickListener(toWOview);
            ((ImageView)findViewById(R.id.profPic)).setClickable(true);
            ((ImageView)findViewById(R.id.profPic)).setOnClickListener(toWOview);

            //load the details of the workout
            loadExercises();


        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fills the scroll layout with workout details
     */
    private void loadExercises(){
        try {
            //My json parser doesn't work here... sad face...
            JSONObject obj = new JSONObject(workout.toString());
            JSONArray ja = new JSONArray(obj.getString("eIDs"));


            for(int i = 0; i < ja.length(); i++){
                final int j = i;
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            LinearExercyse ex = new LinearExercyse(j, CommunityWOActivity.this, response, baseList.get(obj.getInt("baseId")-1).toString());
                            ex.setColor(parseColor(user.getString("color")));
                            scroll.addView(ex.getLayout());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                StringRequest request = new ExercyseRequest("getEx", responseListener);
                ((ExercyseRequest)request).addParam("detail", ja.get(i).toString());
                ((ExercyseRequest)request).addParam("username", user.getString("username"));

                RequestQueue queue = Volley.newRequestQueue(CommunityWOActivity.this);
                queue.add(request);
            }
        }catch(Exception e){e.printStackTrace();}


    }

    /**
     * Switches between page contents and comments tabs
     */
    private void switchTab(){
        //empty the scroll layout first
        scroll.removeAllViews();

        TextView label = (TextView)findViewById(R.id.tabLabel);
        if(commentTab){
            System.out.println("To Page");
            label.setText("Exercyse List");
            loadExercises();
        }else{
            System.out.println("To Comments");
            label.setText("Comments");
            loadComments();
        }
        commentTab = !commentTab;
    }

    /**
     * Fill scroll layout with comments
     */
    private void loadComments(){
        CommentFiller cf = new CommentFiller(CommentFiller.type.WORKOUT, parseInt(workout.get("id")), CommunityWOActivity.this, scroll);
    }
    /**
     * A method that parses a color integer based on a given string.
     * @param name name of the color to be parsed
     * @return integer value of the name of the color
     */
    private int parseColor(String name){
        String name2 = name.toLowerCase();
        int color = 0;
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
            default:
                color = -8712930;
        }
        return color;
    }

    /**
     * Every exercyse is based on a specific exercise. This gets those bases
     */
    private void getBaseList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    baseList = new JSONArray(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request = new ExercyseRequest("getBaseList", responseListener);

        RequestQueue queue = Volley.newRequestQueue(CommunityWOActivity.this);
        queue.add(request);
    }
}
