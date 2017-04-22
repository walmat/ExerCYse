package workout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;

import java.util.ArrayList;

class WorkoutAdapter extends ArrayAdapter<WorkoutObject> {
    WorkoutAdapter(Context context, ArrayList<WorkoutObject> workouts) {
        super(context, 0, workouts);
    }

    /**
     * The view for the workout adapter
     * @param position position of item to be displayed
     * @param convertView the view its being customized from
     * @param parent the parent viewgroup
     * @return the custom view
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        WorkoutObject workout = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_workout_list_view, parent, false);
        }
        TextView wkName = (TextView) convertView.findViewById(R.id.workout_name);
        TextView wkDesc = (TextView) convertView.findViewById(R.id.workout_description);
        TextView wkTime = (TextView) convertView.findViewById(R.id.workout_time);


        if (workout != null) {
            wkTime.setText(getTime(workout.getTime()));
            wkName.setText(workout.getName());
        }
        return convertView;
    }

    private String getTime(int time) {
        if (time < 60) {
            String timeStr = Integer.toString(time);
            return "00:" + timeStr;
        } else {
            int mins = 0;
            while (time > 60) {
                time -= 60;
                mins++;
            }
            String minsStr = Integer.toString(mins);
            String timeStr = Integer.toString(time);
            return minsStr + ":" + timeStr;
        }
    }
}