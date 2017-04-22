package community;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.HomeActivity;
import com.example.jakegrace.exercyse.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.graphics.Color.parseColor;

/**
 * Created by Nate on 4/5/2017.
 */

public class CommentFiller {
    private Session session;
    public static JSONArray comments = null;
    public static Context context;
    public LinearLayout scroll;
    public enum type{
        PLAYLIST,
        WORKOUT
    }
    final Button submit;
    final TextView title;
    EditText eComment;
    type pageType;
    int id;
    boolean addCommentOpen = false;

    public CommentFiller(type pageType, int id, Context c, LinearLayout s){
        this.pageType = pageType;
        this.id = id;
        context = c;
        scroll = s;
        scroll.removeAllViews();
        session = new Session(context.getApplicationContext());

        submit = new Button(context);
        title = new TextView(context);
        getComments(this.id);
    }

    private void getComments(final int id){

        submit.setText("Submit");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(eComment, id+"");
                scroll.removeView(submit);
                scroll.removeView(eComment);
                title.setText("  + Comment");
                addCommentOpen = false;
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    comments = new JSONArray(response);

                    loadComments();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request;
        if(pageType == type.PLAYLIST){
            request = new AllCommentRequests("getPlaylistComments", responseListener);
        }else{
            request = new AllCommentRequests("getWorkoutComments", responseListener);
        }
        ((AllCommentRequests)request).addParam("id",id+"");

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void loadComments(){
        try {
            LinearLayout layout = new LinearLayout(context);

            title.setTextSize(43);
            title.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            title.setTextColor(parseColor("#A0A0A0"));
            title.setText("  + Comment");
            title.setClickable(true);
            View.OnClickListener toWOview = new View.OnClickListener() {
                public void onClick(View v) {
                    doComments(title);
                }
            };
            title.setOnClickListener(toWOview);

            layout.addView(title);
            scroll.addView(layout);



            for (int i = 0; i < comments.length(); i++) {
                JSONObject com = comments.getJSONObject(i);

                System.out.println(com.toString());
                LinearComment lc = new LinearComment(context, com.toString(), this);
                scroll.addView(lc.getLayout());
            }
            if(comments.length() == 0){
                System.out.println("No comments.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void doComments(final TextView view){


        if(!addCommentOpen){
            try {
                eComment = new EditText(context);
                eComment.setTextSize(18);
                eComment.setText("Type here");
                eComment.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                eComment.setTextColor(parseColor("#A0A0A0"));
                view.setText("Close");


                scroll.addView(eComment, scroll.indexOfChild(view));
                scroll.addView(submit, scroll.indexOfChild(view));
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            scroll.removeView(eComment);
            view.setText("  + Comment");
            scroll.removeView(submit);
        }
        addCommentOpen = !addCommentOpen;
    }
    public void submitComment(EditText comment,final String id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //submits a comment
                    System.out.println(response.toString());
                    reload();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request;
        if(pageType == type.PLAYLIST){
            request = new AllCommentRequests("addPlaylistComment", responseListener);
        } else{
            request = new AllCommentRequests("addWorkoutComment", responseListener);
        }
        ((AllCommentRequests)request).addParam("user", session.getUsername());
        ((AllCommentRequests)request).addParam("uID", session.getID());
        ((AllCommentRequests)request).addParam("id", id.toString());
        ((AllCommentRequests)request).addParam("content", comment.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
    public void reload(){
        CommentFiller cf = new CommentFiller(pageType, id, context, scroll);
    }
}
