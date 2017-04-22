package loginregister;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

class LogoutRequest extends StringRequest {
    private static final String LOGOUT_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/login/logout.php";
    private Map<String, String> params;
    public LogoutRequest(Response.Listener<String> listener) {
        super(Request.Method.POST, LOGOUT_REQUEST_URL, listener, null);
    }
}