package workout.volleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake Grace on 03/14/17.
 */

public class GetExercyseRequest extends StringRequest {
    private static final String WORKOUT_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/exercise/exercise.php";
    private final Map<String, String> params;

    /**
     * Gets the base exercises from the default list
     * @param toggle the toggle that is used in the php to determine action
     *               toggle options
     *               'getBaseList' returns the full base exercise list
     *               'getEx' and pass the 'username' and 'detail' exId
     * @param listener the response listener
     */
    public GetExercyseRequest(String username, String detail, String toggle, Response.Listener<String> listener) {
        super(Method.POST, WORKOUT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", toggle);
        params.put("detail", detail);
        params.put("username", username);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
