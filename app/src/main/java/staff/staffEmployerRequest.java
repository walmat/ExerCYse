package staff;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 3/23/2017.
 */

public class staffEmployerRequest extends StringRequest {
    private static final String staffEmployerURL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/staff.php";
    private Map<String, String> params;

    public staffEmployerRequest(Response.Listener listener, Map<String, String> params) {
        super(Method.POST, staffEmployerURL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        this.params = params;
    }

    @Override
    public Map<String, String> getParams() { return params; }

}
