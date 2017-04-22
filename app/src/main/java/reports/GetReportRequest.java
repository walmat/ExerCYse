package reports;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


/**
 * Created by Joe on 2/21/2017.
 */

/**
 * Custom StringRequest to be used by the report activity to recieve all reports from the database.
 */
public class GetReportRequest extends StringRequest {

    private static final String getReportURL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/get_report.php";

    /**
     * Constructor for GetReportRequest.
     * @param listener response listener to be used by GetReportRequest
     */
    public GetReportRequest(Response.Listener<String> listener) {
        super(Method.GET, getReportURL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
