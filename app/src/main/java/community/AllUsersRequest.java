package community;

/**
 * Created by Nate on 2/22/2017.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


class AllUsersRequest extends StringRequest {
    private static final String COMMUNITY_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/users/users.php";
    private final Map<String, String> params;

    public AllUsersRequest(String toggle, Response.Listener<String> listener) {
        super(Method.POST, COMMUNITY_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", toggle);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Add a parameter to the request
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        params.put(key, value);
    }
}