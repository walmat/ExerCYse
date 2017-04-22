package community;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONObject;

/**
 * Created by Nate on 4/4/2017.
 */

public class LinearPlaylist {
    JSONObject user;
    Context context;
    LinearLayout layout;
    public LinearPlaylist(Context cont, String json){
        try {
            context = cont;
            user = new JSONObject(json);

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
        innerParams.width = 500;

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);

        try{
            ImageView pic = new ImageView(context);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(20,20,20,20);
            pic.setBackgroundResource(R.drawable.music_icon);
            pic.setLayoutParams(buttonParams);
            pic.getLayoutParams().width = 90;
            pic.getLayoutParams().height = 90;
            layout.addView(pic);

            TextView title = new TextView(context);
            title.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            title.setTextColor(Color.parseColor("#FFFFFF"));
            if(user.getString("title").length() > 14){
                title.setText(user.getString("title").substring(0,12)+"...");
            }else {
                title.setText(user.getString("title"));
            }
            title.setTextSize(24);
            title.getLayoutParams().width = 300;
            title.getLayoutParams().height = 90;
            layout.addView(title);

            TextView desc = new TextView(context);
            desc.setLayoutParams(innerParams);
            desc.setTextColor(Color.parseColor("#FFFFFF"));
            if(user.getString("text").length() > 18) {
                desc.setText(user.getString("text").substring(0, 15) + "...");
            }else{
                desc.setText(user.getString("text"));
            }
            desc.setTextSize(24);
            layout.addView(desc);



            layout.setClickable(true);

            View.OnClickListener toWOview = new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        addWeeklyView(user.getString("id"));
                    }catch(Exception e){}

                    Intent toView = new Intent(context, PlaylistView.class);

                    toView.putExtra("model",user.toString());
                    context.startActivity(toView);


                }
            };
            layout.setOnClickListener(toWOview);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public LinearLayout getLayout(){return layout;}
    private void addWeeklyView(String id) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //adds a weekly view to the workout
            }
        };
        StringRequest request = new AllPlaylistRequests("view", responseListener);
        ((AllPlaylistRequests) request).addParam("id", id);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
