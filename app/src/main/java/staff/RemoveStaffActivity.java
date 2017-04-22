package staff;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RemoveStaffActivity extends AppCompatActivity {
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_staff);

        etEmail = (EditText) findViewById(R.id.et_removeEmail);
        setUpbtnRemove();
    }

    private void setUpbtnRemove() {
        Button btnRemove = (Button) findViewById(R.id.btn_removeStaff);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });
    }

    private void makeRequest() {
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONresponse = new JSONObject(response);
                    if(!JSONresponse.getBoolean("success")) {
                        new AlertDialog.Builder(RemoveStaffActivity.this)
                                .setMessage(JSONresponse.getString("error"))
                                .setPositiveButton("OK", null).show();
                    }
                    else {
                        Intent intent = new Intent(RemoveStaffActivity.this, staffEmployerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("toggle", "removeStaffEmployer");
        params.put("email", etEmail.getText().toString());

        staffEmployerRequest request = new staffEmployerRequest(listener, params);
        RequestQueue queue = Volley.newRequestQueue(RemoveStaffActivity.this);
        queue.add(request);
    }
}
