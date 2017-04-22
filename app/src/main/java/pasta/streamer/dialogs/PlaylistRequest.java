package pasta.streamer.dialogs;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class PlaylistRequest extends StringRequest {
    private static final String PLAYLIST_REQUEST_URL = "http://proj-309-yt-6.cs.iastate.edu/ExerCYse/users/playlist.php";
    private final Map<String, String> params;
    public PlaylistRequest(String toggle, String username, String name, String id, Response.Listener<String> listener) {
        super(Method.POST, PLAYLIST_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("toggle", toggle);
        params.put("username", username);
        params.put("name", name);
        params.put("id", id);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}