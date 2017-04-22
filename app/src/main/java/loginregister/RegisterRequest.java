package loginregister;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/login/register.php";
    private final Map<String, String> params;
    public RegisterRequest(String firstname, String lastname, String username, String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}