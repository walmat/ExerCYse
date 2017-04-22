package reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;

import java.util.ArrayList;

/**
 * Created by Joe on 3/13/2017.
 */

public class EquipmentReportAdapter extends ArrayAdapter<EquipmentReport> {

    public EquipmentReportAdapter(Context context, ArrayList<EquipmentReport> reports) {
        super(context, 0, reports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EquipmentReport report = getItem(position);
        TextView tvEquipment;
        TextView tvFloor;
        TextView tvNotes;

        if(null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_equipment_report, parent, false);
        }

        tvEquipment = (TextView) convertView.findViewById(R.id.tvEquiptment);
        tvFloor = (TextView) convertView.findViewById(R.id.tvFloor);
        tvNotes = (TextView) convertView.findViewById(R.id.tvNotes);

        tvEquipment.setText("Equipment: " + report.getInfo().get("equipment"));
        tvFloor.setText("Floor: " + report.getInfo().get("floor"));
        tvNotes.setText("Notes: " + report.getInfo().get("notes"));

        return convertView;
    }
}
