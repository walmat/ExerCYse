package workout.volleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import workout.exercise.ExercyseObject;

/**
 * Created by Jake Grace on 03/14/17.
 */
public class SendExercyseRequest extends StringRequest{
    private static final String WORKOUT_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/exercise/exercise.php";
    private final Map<String, String> params;


    /**
     * Sends an exercyse to the php on the server to be put in the database
     * @param username the username
     * @param e the exercyse to be sent
     * @param toggle the php toggle to determine the action taken
     *               the toggle options are as follows
     *                  'addEx' add an ex
     *                  'delEx' delete
     *                  'update' update the given ex
     * @param listener response listener
     */
    public SendExercyseRequest(String username, ExercyseObject e, String wId, String toggle, Response.Listener<String> listener) {
        super(Method.POST, WORKOUT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", e.getName());
        System.out.println(e.getName());
        params.put("sets", Integer.toString(e.getNumSets()));
        System.out.println(e.getNumSets());
        params.put("reps", Integer.toString(e.getNumReps()));
        System.out.println(e.getNumReps());
        params.put("wt", Integer.toString(e.getWeight()));
        System.out.println(e.getWeight());
        params.put("baseId", e.getBaseId());
        System.out.println(e.getBaseId());
        params.put("username", username);
        System.out.println(username);
        params.put("toggle", toggle);
        System.out.println(toggle);
        params.put("wID" ,wId );
        System.out.println(wId);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
