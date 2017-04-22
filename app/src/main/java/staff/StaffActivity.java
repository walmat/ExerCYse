package staff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.HomeActivity;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Home screen for Staff feature. Accessible only by employers.
 */
public class StaffActivity extends AppCompatActivity {
    /**
     * ListView for displaying all current staff members.
     */
    private ListView lvStaff;
    /**
     * Button for switching to AddStaffActivity.
     */
    private Button btnToStaff;
    /**
     * Button for switching to HomeActivity.
     */
    private Button btnBackHome;
    /**
     * ArrayAdapter to be used by lvStaff.
     */
    private ArrayAdapter adapter;
    /**
     * ArrayList to be used by adapter.
     */
    private ArrayList<String> staff;

    /**
     * onCreate for StaffActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        setUplvStaff();
        setUpbtnStaff();
        setUpbtnBackHome();
        setUpRequest();
    }

    /**
     * Helper method called by onCreate that initializes lvStaff and assigns an ArrayAdapter to lvStaff.
     */
    private void setUplvStaff() {
        lvStaff = (ListView)findViewById(R.id.lv_Staff);
        staff = new ArrayList<>();
        adapter = new ArrayAdapter(StaffActivity.this, android.R.layout.simple_list_item_1, staff);
        lvStaff.setAdapter(adapter);
    }

    /**
     * Helper method called by onCreate that creates the StringRequest for getting all current staff members.
     */
    private void setUpRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //respone: "fn0":"firstname", "ln0": "lastname", "em0":"email", "tp0":"type"(1,2, or 3)
                try {
                    JSONObject JSONresponse = new JSONObject(response);

                    if(JSONresponse.getBoolean("empty")) {
                        adapter.clear();
                    } else {
                        int i = 0;
                        String user;
                        while(JSONresponse.has("fn" + i)) {
                            user = "Name: " + JSONresponse.getString("fn" + i) + " " + JSONresponse.getString("ln" + i)
                                    + "\nEmail: " + JSONresponse.getString("em" + i) + "\nType: " + JSONresponse.getString("tp" + i);
                            adapter.add(user);
                            i++;
                        }
                    }
                }
                catch (JSONException e) {

                }
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/temp_get_users.php"
                , listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(StaffActivity.this);
        queue.add(request);
    }

    /**
     * Helper method called by onCreate that initializes btnStaff and defines its behavior when clicked.
     * When clicked the user will be taken to the AddStaffActivity.
     */
    private void setUpbtnStaff() {
        btnToStaff = (Button)findViewById(R.id.btn_addStaff);
        btnToStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent toAddStaff = new Intent(StaffActivity.this, AddStaffActivity.class);
                StaffActivity.this.startActivity(toAddStaff);
            }
        });

    }

    /**
     * Helper method that initializes btnBackHome and defines its behavior when clicked.
     * When clicked the activity will close and the user will be taken back to the home screen.
     */
    private void setUpbtnBackHome() {
        btnBackHome = (Button)findViewById(R.id.btn_backToHome);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent home = new Intent(StaffActivity.this, HomeActivity.class);
                StaffActivity.this.startActivity(home);
            }
        });
    }

}