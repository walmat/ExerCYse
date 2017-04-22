package reports;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 2/16/2017.
 */

/**
 * Custom StringRequest to be used by the report activity in order to add a new report to the database.
 */
public class AddReportRequest extends StringRequest {
    private static String add_reportURL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/add_report.php";
    /**
     * Map containing parameters for the request sent to the server.
     */
    private Map<String, String> arParams;

    /**
     * Constructor for AddReportRequest.
     * @param equipment piece of equipment chosen by the user
     * @param floor floor that the equipment is found on
     * @param notes comments from the user about the broken equipment
     * @param listener the response listener to be used by the StringRequest
     */
    public AddReportRequest(String equipment, String floor, String notes, Response.Listener<String> listener){
        super(Method.POST, add_reportURL, listener, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        arParams = new HashMap<>();
        arParams.put("equipment", equipment);
        arParams.put("floor", floor);
        arParams.put("notes", notes);

    }

    /**
     * Get method for arParams
     * @return arParams
     */
    @Override
    public Map<String, String> getParams() {
        return arParams;
    }
}
