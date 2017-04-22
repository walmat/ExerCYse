package workout.volleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake Grace on 03/05/17.
 */

public class DeleteWorkoutRequest extends StringRequest{

    private static final String WORKOUT_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/workout/workout.php";
    private final Map<String, String> params;

    /**
     * Delete a given workout from the server
     * @param wId the id of the workout to be deleted
     * @param listener response listener
     */
    public DeleteWorkoutRequest(String wId, Response.Listener<String> listener) {
        super(Method.POST, WORKOUT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", "deleteWorkout");
        params.put("workoutID", wId);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
