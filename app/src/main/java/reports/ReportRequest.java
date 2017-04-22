package reports;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 3/21/2017.
 */

public class ReportRequest extends StringRequest {
    private static final String ReportsURL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/reports.php";
    private Map<String, String> params;

    public ReportRequest(Response.Listener<String> listener, Map<String, String> params) {
        super(Method.POST, ReportsURL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        this.params = params;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
