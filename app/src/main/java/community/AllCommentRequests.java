package community;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nate on 4/5/2017.
 */

public class AllCommentRequests extends StringRequest{
    private static final String COMMUNITY_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/community/comments.php";
    private final Map<String, String> params;

    public AllCommentRequests(String toggle, Response.Listener<String> listener){
        super(Request.Method.POST, COMMUNITY_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle",toggle);
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
    public void addParam(String key, String value){
        params.put(key, value);
    }
}
