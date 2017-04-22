package reports;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Joe on 3/11/2017.
 */

public interface ReportInterface {

    public HashMap<String, String> getInfo();
    public void setInfo(HashMap<String, String> info);
}
