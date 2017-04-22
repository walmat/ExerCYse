package staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddStaffActivity extends AppCompatActivity {
    Button btnAddStaff;
    Boolean success;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        etEmail = (EditText)findViewById(R.id.et_staffEmail);
        success = false;

        setUpAddStaff();

    }

    private void setUpAddStaff() {
        btnAddStaff = (Button)findViewById(R.id.btn_addToStaff);
        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();

            }
        });

    }

    private void sendRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONresponse = new JSONObject(response);
                    if(JSONresponse.getBoolean("success")) {
                        final Intent backToStaff = new Intent(AddStaffActivity.this, StaffActivity.class);
                        AddStaffActivity.this.startActivity(backToStaff);
                        finish();
                    } else {
                        new AlertDialog.Builder(AddStaffActivity.this)
                                .setMessage("There don't appear to be any users with that email.")
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        StringRequest addStaffRequest = new StringRequest(Request.Method.POST, "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/add_staff.php"
                , listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", etEmail.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(AddStaffActivity.this);
        queue.add(addStaffRequest);

    }
}
