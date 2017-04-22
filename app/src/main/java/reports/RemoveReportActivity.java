package reports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RemoveReportActivity extends AppCompatActivity {
    private String equipment;
    private String floor;
    private String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_report);

        getExtras(getIntent());
        setTvEquipment();
        setTvFloor();
        setTvNotes();
        setUpBtnRemove();
    }

    private void getExtras(Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) {
            equipment = extras.getString("equipment");
            floor = extras.getString("floor");
            notes = extras.getString("notes");
        } else {
            equipment = null;
            floor = null;
            notes = null;
        }
    }

    private void setTvEquipment() {
        TextView tvEquipment = (TextView)findViewById(R.id.txt_Equip2);
        tvEquipment.setText("Equipment: ");
        if(equipment != null) {
            tvEquipment.setText(tvEquipment.getText() + equipment);
        } else {
            tvEquipment.setText(tvEquipment.getText() + "No equipment found.");
        }
    }

    private void setTvFloor() {
        TextView tvFloor = (TextView)findViewById(R.id.txt_Flr2);
        tvFloor.setText("Floor: ");
        if(floor != null) {
            tvFloor.setText(tvFloor.getText() + floor);
        } else {
            tvFloor.setText(tvFloor.getText() + "No floor found.");
        }
    }

    private void setTvNotes() {
        TextView tvNotes = (TextView)findViewById(R.id.txt_nts2);
        tvNotes.setText("Notes: ");
        if(notes != null) {
            tvNotes.setText(tvNotes.getText() + notes);
        } else {
            tvNotes.setText(tvNotes.getText() + "No notes found.");
        }
    }

    private void setUpBtnRemove() {
        Button btnAccept = (Button) findViewById(R.id.btn_remove);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpRequest();
            }
        });
    }

    private void setUpRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if(response.getBoolean("success")) {
                        Intent leave = new Intent(RemoveReportActivity.this, ReportsHomeActivity.class);
                        startActivity(leave);
                        finish();
                    } else {
                        // TODO: 3/21/2017 make toast
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("toggle", "removeReport");
        params.put("id", getIntent().getExtras().getString("id"));
        ReportRequest request = new ReportRequest(listener, params);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
