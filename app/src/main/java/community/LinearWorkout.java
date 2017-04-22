package community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.example.jakegrace.exercyse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.graphics.Color.parseColor;

/**
 * Created by Nate on 3/21/2017.
 */

public class LinearWorkout {
    JSONObject workouts;
    LinearLayout layout;
    Context context;

    public LinearWorkout(Context cont, String json) {
        try {
            workouts = new JSONObject(json);
            context = cont;
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColor(int color) {
        layout.setBackgroundColor(color);
    }

    private void build() {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        innerParams.width = 400;

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);

//        Button ib = new Button(CommunityActivity.this);
//        ib.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        try {
            ImageView wo = new ImageView(context);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(20, 20, 20, 20);
            wo.setBackgroundResource(R.drawable.workout_icon);
            wo.setLayoutParams(buttonParams);
            wo.getLayoutParams().width = 90;
            wo.getLayoutParams().height = 90;
            // wo.setImageResource(R.drawable.workout_icon);
            layout.addView(wo);

            TextView title = new TextView(context);
            title.setTextSize(24);
            title.setLayoutParams(innerParams);
            title.setTextColor(parseColor("#FFFFFF"));
            //title.setBackgroundColor(Color.parseColor("#A0A0A0"));
            title.setPadding(30, 30, 30, 30);
            title.setText(workouts.getString("name"));
            layout.addView(title);

            JSONArray temp = new JSONArray(workouts.getString("eIDs"));
            TextView ex = new TextView(context);
            ex.setTextSize(24);
            ex.setLayoutParams(innerParams);
            ex.setTextColor(parseColor("#FFFFFF"));
            ex.setText("With " + temp.length() + " ExerCYses");

            layout.addView(ex);
            layout.setBackgroundColor(parseColor("#A0A0A0"));
            layout.setClickable(true);

            View.OnClickListener toWOview = new View.OnClickListener() {
                public void onClick(View v) {
                    Intent toView = new Intent(context, CommunityWOActivity.class);
                    try {
                        addWeeklyView(workouts.getString("id"));
                    }catch(Exception e){}
                    toView.putExtra("model", workouts.toString());
                    context.startActivity(toView);
                }
            };
            layout.setOnClickListener(toWOview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinearLayout getLayout() {
        return layout;
    }

    private void addWeeklyView(String id) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               //adds a weekly view to the workout
            }
        };
        StringRequest request = new AllWorkoutsRequest("view", responseListener);
        ((AllWorkoutsRequest) request).addParam("id", id);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}