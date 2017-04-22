package workout.volleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake Grace on 03/05/17.
 */

public class GetWorkoutsRequest extends StringRequest{

    private static final String WORKOUT_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/workout/workout.php";
    private Map<String, String> params;


    /**
     * Gets all workouts for current user
     * @param workoutname the name of the wkt
     * @param toggle the toggle that is used in the php to determine action
     * @param listener the response listener
     */
    public GetWorkoutsRequest(String username, String workoutname, String toggle, Response.Listener<String> listener) {
        super(Method.POST, WORKOUT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", toggle);
        params.put("username", username);
        params.put("name", workoutname);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
