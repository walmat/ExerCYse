package staff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class staffEmployerActivity extends AppCompatActivity {
    private ArrayAdapter adapter;
    private ArrayList<String> staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_employer);

        setUpLvStaffEmployer();
        setUpRequest();
        setUpbtnAdd();
        setUpbtnRemove();
    }

    private void setUpLvStaffEmployer() {
        ListView lvStaffEmployer = (ListView) findViewById(R.id.lv_staffEmployer);
        staff = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,  staff);
        lvStaffEmployer.setAdapter(adapter);
    }

    private void setUpRequest() {
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject JSONresponse = new JSONObject(response);
                    String user;
                    int i = 0;
                    while(JSONresponse.has("fn" + i)) {
                        user = "Name: " + JSONresponse.getString("fn" + i) + " "
                                + JSONresponse.getString("ln" + i) + "\nEmail: "
                                + JSONresponse.getString("em" + i) + "\nType: ";
                        if(JSONresponse.getString("tp" + i).equals("2")) {
                            user += "Staff";
                            adapter.add(user);
                        }
                        else if(JSONresponse.getString("tp" + i).equals("3")) {
                            user += "Employer";
                            adapter.add(user);
                        }
                        else {
                            user += "User";
                        }
                        i++;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("toggle", "getStaffEmployer");
        staffEmployerRequest request = new staffEmployerRequest(listener, params);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void setUpbtnAdd() {
        Button btnAddStaffEmployer = (Button) findViewById(R.id.btn_addStaffEmployer);
        btnAddStaffEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staffEmployerActivity.this, AddStaffActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpbtnRemove() {
        Button btnRemoveStaffEmployer = (Button) findViewById(R.id.btn_removeStaffEmployer);
        btnRemoveStaffEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staffEmployerActivity.this, RemoveStaffActivity.class);
                startActivity(intent);
            }
        });
    }
}
