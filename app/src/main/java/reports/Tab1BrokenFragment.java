package reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 3/20/2017.
 */

public class Tab1BrokenFragment extends Fragment {
    private View rootView;
    private EquipmentReportAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab1_broken, container, false);

        setUpfab();
        setUplvReports();
        setUpRequest();

        return rootView;
    }

    private void setUpfab() {
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddReport = new Intent(getContext(), NewReportActivity.class);
                getContext().startActivity(toAddReport);
            }
        });
    }

    private void setUplvReports() {
        ArrayList<EquipmentReport> arr = new ArrayList<>();
        adapter = new EquipmentReportAdapter(getContext(), arr);
        final ListView lvReports = (ListView) rootView.findViewById(R.id.lv_ReportsBroken);
        lvReports.setAdapter(adapter);
    }

    private void setUpRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONresponse = new JSONObject(response);
                    if(JSONresponse.has("error")) {
                        adapter.clear();
                    }
                    else {
                        int i = 0;
                        EquipmentReport report;
                        String equipment;
                        String floor;
                        String notes;
                        String id;
                        String uID;
                        while(JSONresponse.has("e" + i)) {
                            equipment = JSONresponse.getString("e" + i);
                            floor = JSONresponse.getString("f" + i);
                            notes = JSONresponse.getString("n" + i);
                            id = JSONresponse.getString("id" + i);
                            uID = JSONresponse.getString("uID" + i);
                            report = new EquipmentReport(equipment, floor, notes, id, uID);
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

        Map<String, String> params = new HashMap<>();
        params.put("toggle", "activeReports");
        StringRequest getReports = new ReportRequest(listener, params);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getReports);
    }
}
