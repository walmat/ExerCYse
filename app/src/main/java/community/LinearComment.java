package community;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Nate on 4/9/2017.
 */

public class LinearComment {
    private Session session;
    CommentFiller cf;
    JSONObject comment;
    Context context;
    LinearLayout layout;
    public LinearComment(Context cont,String comment, CommentFiller cf){
        try {
            this.cf = cf;
            context = cont;
            session = new Session(context.getApplicationContext());
            this.comment = new JSONObject(comment);

            build();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setColor(int color){
        layout.setBackgroundColor(color);
    }
    private void build(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);

        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        innerParams.setMargins(30,20,10,10);
        innerParams.width = 400;

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);

        // layout.setBackgroundResource(R.drawable.metal);



        try{
            LinearLayout layout2 = new LinearLayout(context);
            layout2.setOrientation(LinearLayout.VERTICAL);
            layout2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            TextView username = new TextView(context);
            username.setLayoutParams(innerParams);
            username.setText(comment.getString("username"));
            username.setTextColor(Color.parseColor("#202020"));
            username.setTextSize(24);
            layout2.addView(username);

            String dateTime[] = comment.getString("date").split(" ");
            String dSplit[] = dateTime[0].split("-");
            String tSplit[] = dateTime[1].split(":");
            String half = "am";
            if(Integer.parseInt(tSplit[0]) > 12){
                tSplit[0] = (Integer.parseInt(tSplit[0]) - 12)+"";
                half = "pm";
            }
            String ts = tSplit[0]+":"+tSplit[1]+" "+half;
            ts += "  "+dSplit[1]+"/"+dSplit[2]+"/"+dSplit[0];
            TextView date = new TextView(context);
            date.setLayoutParams(innerParams);
            date.setText(ts);
            date.setTextColor(Color.parseColor("#A0A0A0"));
            date.setTextSize(14);
            layout2.addView(date);

            TextView body = new TextView(context);
            body.setLayoutParams(innerParams);
            body.setText(comment.getString("content"));
            body.setTextColor(Color.parseColor("#202020"));
            body.setTextSize(14);
            layout2.addView(body);

            if(session.getUsername().equals(comment.getString("username")) || Integer.parseInt(session.getType()) > 1) {
                TextView delBTN = new TextView(context);
                delBTN.setLayoutParams(innerParams);
                delBTN.setText("delete");
                delBTN.setTextColor(Color.parseColor("#4286f4"));
                delBTN.setTextSize(14);
                delBTN.setClickable(true);

                delBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteThisComment();
                    }
                });

                layout2.addView(delBTN);
            }

            layout.addView(layout2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public LinearLayout getLayout(){return layout;}
    public void deleteThisComment(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //deleted
                    System.out.println(response.toString());
                    System.out.println("Deleted");
                    cf.reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request;
        if(cf.pageType == CommentFiller.type.PLAYLIST){
            request = new AllCommentRequests("deletePlaylistComment", responseListener);
            try {
                ((AllCommentRequests) request).addParam("plID", comment.getString("id"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            request = new AllCommentRequests("deleteWorkoutComment", responseListener);
            try {
                ((AllCommentRequests) request).addParam("woID", comment.getString("id"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
