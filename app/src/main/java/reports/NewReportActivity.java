package reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity used by users in order to create a new broken equipment report.
 */
public class NewReportActivity extends AppCompatActivity {
    private Spinner spnEquipment;
    private RadioButton rdbFirst;
    private RadioButton rdbSecond;
    private RadioGroup floorGroup;
    private EditText notes;
    private Button btnSubmit;


    /**
     * OnCreate method for NewReportActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        rdbFirst = (RadioButton) findViewById(R.id.rdb_First);
        rdbSecond = (RadioButton) findViewById(R.id.rdb_Second);
        spnEquipment = (Spinner) findViewById(R.id.spn_Equipment);
        floorGroup = (RadioGroup) findViewById(R.id.rgp_floorGroup);
        notes = (EditText) findViewById(R.id.et_notes);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        setUpSpnEquipment();
        setUpBtnSubmit();
    }

    /**
     * This method creates an AddReportRequest and adds it to a queue to be sent to the server.
     * It also makes a response listener to be used by the AddReportRequest.
     */
    private void setUpBtnSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String equipment;
                final String floor;
                final String comments;

                equipment = getEquipment();
                floor = getFloor();
                comments = getComments();

                if(equipment != null && floor != null && comments != null) {
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if(jsonResponse.getBoolean("success")) {
                                    Intent toReport = new Intent(NewReportActivity.this, ReportsHomeActivity.class);
                                    NewReportActivity.this.startActivity(toReport);
                                }
                                else {
                                    Toast.makeText(NewReportActivity.this, jsonResponse.getString("error"), Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    Map<String, String> params = new HashMap<>();
                    params.put("equipment", equipment);
                    params.put("floor", floor);
                    params.put("notes", comments);
                    params.put("toggle", "addReport");
                    ReportRequest addReport = new ReportRequest(listener, params);
                    RequestQueue queue = Volley.newRequestQueue(NewReportActivity.this);
                    queue.add(addReport);
                }
            }
        });
    }

    /**
     * Helper method used to get the current selected item of the the spnEquipment
     * Spinner.
     * @return the current selected item of spnEquipment if that item is not empty, otherwise null
     */
    private String getEquipment() {
        if(!spnEquipment.getSelectedItem().toString().isEmpty())
        {
            return spnEquipment.getSelectedItem().toString();
        }
        else
        {
            new AlertDialog.Builder(NewReportActivity.this)
                    .setMessage("All fields must be filled out to Create a report.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();

            return null;
        }
    }

    /**
     * Helper method to determine which floor was selected by user.
     * @return "First" if rdbFirst is checked, "Second if rdbSecond is checked, and null otherwise
     */
    private String getFloor() {
        if(rdbFirst.isChecked())
        {
            return "First";
        }
        else if(rdbSecond.isChecked())
        {
            return "Second";
        }
        else
        {
            new AlertDialog.Builder(NewReportActivity.this)
                    .setMessage("All fields must be filled out to Create a report.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            return null;
        }
    }

    /**
     * Helper method to retrieve comments made in notes.
     * @return the contents of notes if not empty, otherwise null
     */
    private String getComments() {
        if(!notes.getText().toString().isEmpty())
        {
            return notes.getText().toString();
        }
        else
        {
            new AlertDialog.Builder(NewReportActivity.this)
                    .setMessage("All fields must be filled out to Create a report.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            return null;
        }
    }


    /**
     * Helper method that assigns a spinner to spnEquipment and performs
     * necessary set up for spnEquipment to be used.
     */
    private void setUpSpnEquipment(){
        spnEquipment = (Spinner)findViewById(R.id.spn_Equipment);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Equipment_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEquipment.setAdapter(adapter);
        spnEquipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("Bench Press")){
                    Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG);
                    rdbFirst.toggle();
                    floorGroup.check(R.id.rdb_First);
                }
                if(parent.getItemAtPosition(position).toString().equals("Crunch Machine")){
                    floorGroup.check(R.id.rdb_Second);
                    rdbSecond.toggle();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
