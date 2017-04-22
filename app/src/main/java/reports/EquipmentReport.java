package reports;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Joe on 3/12/2017.
 */

public class EquipmentReport implements ReportInterface {

    private HashMap<String, String> info;

    public EquipmentReport(HashMap<String, String> info) {
        this.info = info;
    }

    public EquipmentReport(String equipment, String floor, String notes, String id, String uID) {
        info = new HashMap<>();
        info.put("equipment", equipment);
        info.put("floor", floor);
        info.put("notes", notes);
        info.put("id", id);
        info.put("uID", uID);
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }

    public HashMap<String, String> getInfo() {
        return info;
    }
}
