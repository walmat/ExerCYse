package reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity for user to view a list of the broken equipment reports. Normal users
 * will only be able to see a list of current broken machines. Staff and employers
 * will be able to view new unverified reports as well as complete details of all
 * active reports.
 */
public class ViewReportActivity extends AppCompatActivity {
    private Button btnBack;
    private Button btnRefresh;
    private ListView lvReports;
    private EquipmentReportAdapter adapter;
    private ArrayList<EquipmentReport> arr;

    /**
     * onCreate for ViewReportActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        setUpbtnBack();
        setUplvReports();
        setUpRequest();


    }

    /**
     * Assigns button to btnBack and sets on click listener for btnBack.
     */
    private void setUpbtnBack() {
        btnBack = (Button)findViewById(R.id.btn_BackToReport);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toReport = new Intent(ViewReportActivity.this, ReportActivity.class);
                ViewReportActivity.this.startActivity(toReport);
            }
        });
    }

    /**
     * Sets up the ArrayAdapter to be used by lvReports and assigns an ArrayList arr to the adapter.
     */
    private void setUplvReports() {
        arr = new ArrayList<>();
        adapter = new EquipmentReportAdapter(this, arr);
        lvReports = (ListView)findViewById(R.id.lv_Reports);
        lvReports.setAdapter(adapter);
    }

    /**
     * Defines what happens on receiving response from the server. Creates a GetReportRequest and adds it to a new queue.
     */
    private void setUpRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONresponse = new JSONObject(response);
                    if(JSONresponse.getBoolean("empty")) {
                        adapter.clear();
                    }
                    else {
                        int i = 0;
                        EquipmentReport report;
                        String equipment;
                        String floor;
                        String notes;
                        while(JSONresponse.has("e" + i)) {
                            equipment = JSONresponse.getString("e" + i);
                            floor = JSONresponse.getString("f" + i);
                            notes = JSONresponse.getString("n" + i);
                            report = new EquipmentReport(equipment, floor, notes, "placeholder", "placeholder");
                            adapter.add(report);
                            i++;
                        }

                    }

                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest getReports = new GetReportRequest(listener);
        RequestQueue queue = Volley.newRequestQueue(ViewReportActivity.this);
        queue.add(getReports);
    }
}
