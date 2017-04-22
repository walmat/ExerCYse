package community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

/**
 * Created by Nate on 4/4/2017.
 */

public class PlaylistView extends AppCompatActivity {
    JSONObject playlist = null;
    JSONObject user = null;
    LinearLayout scroll;
    boolean commentTab = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_view_activity);


        try {
            //Create JSONObjects
            String playlistString = this.getIntent().getStringExtra("model");
            playlist = new JSONObject(playlistString);
             getUserInfo(playlist.getString("uID"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Loads the static page elements that do not change with user and workout infor
     */
    private void loadViews(){
        try {
            //create instances
            scroll = (LinearLayout) findViewById(R.id.scroll);
            TextView title = (TextView) findViewById(R.id.title);
            TextView author = (TextView) findViewById(R.id.author);
            LinearLayout workBG = (LinearLayout) findViewById(R.id.workBG);

            //fill profile picture
            ImageView profPic = (ImageView) findViewById(R.id.profPic);
            PicFetcher p = new PicFetcher(profPic, user.getString("username"));
            p.start();

            //create clickables that direct to view user profile page
            title.setText(playlist.getString("title"));
            author.setText("By " + user.getString("firstname") + " " + user.getString("lastname"));
            workBG.setBackgroundColor(parseColor(user.get("color").toString()));

            author.setClickable(true);

            View.OnClickListener toWOview = new View.OnClickListener() {
                public void onClick(View v) {
                    Intent toView = new Intent(PlaylistView.this, UserViewActivity.class);
                    toView.putExtra("model",user.toString());
                    PlaylistView.this.startActivity(toView);
                }
            };
            author.setOnClickListener(toWOview);
            ((ImageView)findViewById(R.id.profPic)).setClickable(true);
            ((ImageView)findViewById(R.id.profPic)).setOnClickListener(toWOview);

            //tab switch
            final ImageView tab = (ImageView) findViewById(R.id.bannerBreak);
            tab.setClickable(true);
            View.OnClickListener switcher = new View.OnClickListener() {
                public void onClick(View v) {
                    switchTab();
                }
            };
            tab.setOnClickListener(switcher);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            label.setText("Song List");
            loadSongs();
        }else{
            System.out.println("To Comments");
            label.setText("Comments");
            loadComments();
        }
        commentTab = !commentTab;
    }

    /**
     * Loads the songs in the playlist
     */
    private void loadSongs(){

    }

    /**
     * Loads the comments that were made on this page
     */
    private void loadComments(){
        try {
            CommentFiller cf = new CommentFiller(CommentFiller.type.PLAYLIST, playlist.getInt("id"),PlaylistView.this, scroll);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Gets creator's information
     * @param id user id
     */
    private void getUserInfo(String id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    user = new JSONObject(response);

                    //fill views with info
                    loadViews();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request = new AllUsersRequest("getUserProfile", responseListener);
        ((AllUsersRequest)request).addParam("user_info", id);

        RequestQueue queue = Volley.newRequestQueue(PlaylistView.this);
        queue.add(request);
    }
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
}
