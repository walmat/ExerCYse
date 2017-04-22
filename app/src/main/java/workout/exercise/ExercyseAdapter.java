package workout.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jake Grace on 03/04/17.
 * A custom adapter for any ListView that wants to display a list of exercyses
 */
public class ExercyseAdapter extends ArrayAdapter<BaseExercyseObject>{
    public ExercyseAdapter(Context context, ArrayList<BaseExercyseObject> exercyses){
        super(context, 0, exercyses);
    }


    /**
     * The view that will be used by the ListView to see the list of exercyses
     * @param position the position of the exercyse
     * @param convertView
     * @param parent
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BaseExercyseObject ex = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_exercyse_list_view, parent, false);
        }

        TextView exName = (TextView) convertView.findViewById(R.id.tvExName);
        TextView exDesc = (TextView) convertView.findViewById(R.id.tvExDesc);
        TextView exSets = (TextView) convertView.findViewById(R.id.tvSets);
        TextView exReps = (TextView) convertView.findViewById(R.id.tvReps);
        TextView exWeight = (TextView) convertView.findViewById(R.id.tvWt);
        TextView exTime = (TextView) convertView.findViewById(R.id.tvExTime);

        ImageView exImg = (ImageView) convertView.findViewById(R.id.imgEx);

        Picasso.with(getContext()).load(ex.getImageString()).placeholder(R.mipmap.ic_launcher).into(exImg);

        exName.setText(ex.getName());
        exDesc.setText(ex.getDescription());

        try {
            ExercyseObject exObj = (ExercyseObject) ex;
            String sets = Integer.toString(exObj.getNumSets());
            String reps = Integer.toString(exObj.getNumReps());
            String wt = Integer.toString(exObj.getWeight());
            int time = exObj.getTime();
            exReps.setText(reps + " reps");
            exSets.setText(sets + " sets");
            exTime.setText(getTime(time));

            if (exObj.getWeight() > 0) {
                exWeight.setText(wt + " lbs");

            }
        } catch (Exception ignored) {
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
