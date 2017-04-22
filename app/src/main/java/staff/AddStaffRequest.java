package staff;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 3/9/2017.
 */

/**
 * Custom String request used by AddStaffActivity to make a user a staff member.
 */
public class AddStaffRequest extends StringRequest {
    /**
     * Map containing parameters for the request sent to the server.
     */
    private Map<String, String> params;

    /**
     * Constructor that sets up params.
     * @param email email of user to be made a staff member
     * @param listener Response Listener to be used by the StringRequest
     */
    public AddStaffRequest(String email, Response.Listener listener) {
        super(Method.POST, "", listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        params = new HashMap<>();
        params.put("email", email);
    }

    /**
     * Get method for params
     * @return params
     */
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
