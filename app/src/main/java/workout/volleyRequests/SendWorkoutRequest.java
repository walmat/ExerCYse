package workout.volleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import workout.WorkoutObject;

/**
 * Created by Jake Grace on 03/05/17.
 */

public class SendWorkoutRequest extends StringRequest{


    private static final String WORKOUT_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/workout/workout.php";
    private final Map<String, String> params;


    /**
     * Sends a workout to the php on the server to be put in the database
     * @param username the username
     * @param w the workout to be sent
     * @param toggle the php toggle to determine the action taken
     * @param listener response listener
     */
    public SendWorkoutRequest(String username, WorkoutObject w, String toggle, Response.Listener<String> listener) {
        super(Method.POST, WORKOUT_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("name", w.getName());
        //params.put("eIDs", w.getExercyseIds());
        params.put("username", username);
        params.put("toggle", toggle);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
