package reports;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class Tab3ActiveFragment extends Fragment {
    private View rootView;
    private EquipmentReportAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab3_active, container, false);

        setUplvReportsActive();
        setUpRequest();

        return rootView;
    }

    private void setUplvReportsActive() {
        ArrayList<EquipmentReport> arr = new ArrayList<>();
        adapter = new EquipmentReportAdapter(getContext(), arr);
        ListView lvReports = (ListView) rootView.findViewById(R.id.lv_ReportsActive);
        lvReports.setAdapter(adapter);
        lvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RemoveReportActivity.class);
                intent.putExtra("equipment", adapter.getItem(position).getInfo().get("equipment"));
                intent.putExtra("floor", adapter.getItem(position).getInfo().get("floor"));
                intent.putExtra("notes", adapter.getItem(position).getInfo().get("notes"));
                intent.putExtra("id", adapter.getItem(position).getInfo().get("id"));
                getContext().startActivity(intent);
            }
        });
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
