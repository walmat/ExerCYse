package community;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nate on 3/19/2017.
 */

public class UserViewActivity extends AppCompatActivity {
    JSONObject user = null;
    JSONArray workouts = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        String userString = this.getIntent().getStringExtra("model");
        try {
            System.out.println(userString);
            user = new JSONObject(userString);
            getUserInfo(user.getString("username"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getUserInfo(String id) {
        //getUserProfile(id)
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    user = new JSONObject(response);

                    loadViews();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request = new AllUsersRequest("getProfileByName", responseListener);
        ((AllUsersRequest) request).addParam("username", id);

        RequestQueue queue = Volley.newRequestQueue(UserViewActivity.this);
        queue.add(request);
    }
    public void loadViews() {
        try {
            System.out.println("asdf  " + user);

            LinearLayout profBG = (LinearLayout) findViewById(R.id.userBG);
            LinearLayout banner = (LinearLayout) findViewById(R.id.exercyseBanner);
            TextView username = (TextView) findViewById(R.id.userName);
            TextView bannerText = (TextView) findViewById(R.id.bannerText);
            TextView name = (TextView) findViewById(R.id.name);
            TextView usertype = (TextView) findViewById(R.id.userType);
            ImageView profPic = (ImageView) findViewById(R.id.profPic);
            TextView bio = (TextView) findViewById(R.id.bio);

            PicFetcher p = new PicFetcher(profPic, user.getString("username"));
            p.start();
            // getBitmapFromURL(profPic, user.getString("username"));

            username.setText(user.getString("username"));

            //1 student
            //2 admin
            //3 Employer
            switch (user.getString("type")) {
                case "1":
                    usertype.setText("Student");
                    break;
                case "2":
                    usertype.setText("Admin");
                    break;
                case "3":
                    usertype.setText("Employer");
                    break;
                default:
                    usertype.setText("A Beast of No Nation");
                    break;
            }

            JSONObject obj = new JSONObject(user.toString());
            System.out.println("Bio: " + obj.get("bio").equals(null));
            if (obj.get("bio").equals(null)) {
                System.out.println("So set text for bio to null");
                bio.setText("This user does not want to tell you anything about him/her self.");
            } else {
                bio.setText(obj.getString("bio"));
            }

            name.setText(user.getString("firstname") + " " + user.getString("lastname"));
            if (user.get("color").equals(null)) {
                banner.setBackgroundColor(parseColor("red"));
                profBG.setBackgroundColor(parseColor("red"));
            } else {
                banner.setBackgroundColor(parseColor(user.getString("color")));
                profBG.setBackgroundColor(parseColor(user.getString("color")));
            }
            bannerText.setText(user.getString("username") + "\'s Workouts");
            getWorkouts(user.getString("username"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * A method that parses a color integer based on a given string.
     * @param name name of the color to be parsed
     * @return integer value of the name of the color
     */
    private int parseColor(String name) {
        String name2 = name.toLowerCase();
        int color = -8712930;
        //integer values for differing color values
        switch (name2) {
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
    private void setColor(LinearLayout linearLayout, String colorName) {

        linearLayout.setBackgroundColor(parseColor(colorName));
        linearLayout.refreshDrawableState();
        linearLayout.invalidate();
    }

    private void getWorkouts(String username) {
        //getUserProfile(id)
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    workouts = new JSONArray(response);

                    loadWorkouts();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest request = new AllWorkoutsRequest("getWorkoutsFromUser", responseListener);
        ((AllWorkoutsRequest) request).addParam("username", username);

        RequestQueue queue = Volley.newRequestQueue(UserViewActivity.this);
        queue.add(request);
    }

    public void loadWorkouts() {
        System.out.println("Workout list//////////////////////////////\n" + workouts.toString());

        try {
            if (workouts.length() == 0) {
                TextView bannerText = (TextView) findViewById(R.id.bannerText);
                bannerText.setText(user.getString("username") + " has no Workouts :(");
            } else {
                LinearLayout scroll = (LinearLayout) findViewById(R.id.scroll);
                for (int i = 0; i < workouts.length(); i++) {
                    // Button ib = new Button(UserViewActivity.this);
                    //ib.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    // ib.setText("asdfadf");
                    JSONObject obj = workouts.getJSONObject(i);
                    System.out.println(obj.toString());
                    LinearWorkout lw = new LinearWorkout(UserViewActivity.this, obj.toString());
                    lw.setColor(parseColor(user.getString("color")));
                    scroll.addView(lw.getLayout());
                    System.out.println("Added a LinearWorkout");

                    //scroll.addView(ib);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getBitmapFromURL(final ImageView image, final String username) {

        UserViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://proj-309-yt-6.cs.iastate.edu/ExerCYse/" + username + ".txt");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    image.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
