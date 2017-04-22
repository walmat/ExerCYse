package profile;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileRequest extends StringRequest {
    private static final String PROFILE_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/users/users.php";
    private final Map<String, String> params;
    public ProfileRequest(String user_info, String toggle, Response.Listener<String> listener) {
        super(Request.Method.POST, PROFILE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", toggle);
        params.put("user_info", user_info);
    }
    public ProfileRequest(String username, String change, String toggle, Response.Listener<String> listener) {
        super(Request.Method.POST, PROFILE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", toggle);
        params.put("change", change);
        params.put("user_info", username);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}