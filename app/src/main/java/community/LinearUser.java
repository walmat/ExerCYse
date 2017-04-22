package community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;

import org.json.JSONObject;

/**
 * Created by Nate on 3/22/2017.
 */

public class LinearUser {
    JSONObject user;
    Context context;
    LinearLayout layout;

    public LinearUser(Context cont, String json) {
        try {
            context = cont;
            user = new JSONObject(json);

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
        innerParams.setMargins(30, 20, 10, 10);
        innerParams.width = 400;

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);

        try {
            ImageView profPic = new ImageView(context);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(20, 20, 20, 20);
            profPic.setBackgroundResource(R.drawable.profstock);
            profPic.setLayoutParams(buttonParams);
            profPic.getLayoutParams().width = 90;
            profPic.getLayoutParams().height = 90;
            layout.addView(profPic);

            TextView username = new TextView(context);
            username.setLayoutParams(innerParams);
            username.setTextColor(Color.parseColor("#FFFFFF"));
            username.setText(user.getString("username"));
            username.setTextSize(24);
            layout.addView(username);

            TextView userType = new TextView(context);
            userType.setLayoutParams(innerParams);
            userType.setTextColor(Color.parseColor("#FFFFFF"));
            userType.setTextSize(24);

            String type = user.getString("type");
            if (type.equals("2")) {
                userType.setText("(Admin)");
                layout.addView(userType);
            }
            if (type.equals("3")) {
                userType.setText("(Employer)");
                layout.addView(userType);
            }

            PicFetcher pf = new PicFetcher(profPic, user.getString("username"));
            pf.start();

            layout.setClickable(true);

            View.OnClickListener toWOview = new View.OnClickListener() {
                public void onClick(View v) {
                    Intent toView = new Intent(context, UserViewActivity.class);

                    toView.putExtra("model", user.toString());
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
}
