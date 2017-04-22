package community;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jakegrace.exercyse.R;

import org.json.JSONObject;

/**
 * Created by Nate on 3/22/2017.
 */

public class LinearExercyse {
    JSONObject exercyse;
    JSONObject base;
    Context context;
    LinearLayout layout;
    int number;

    public LinearExercyse(int number, Context cont, String exercyse, String base) {
        System.out.println("EX upper: " + exercyse);
        System.out.println("EX base: " + base);
        try {
            this.number = number;
            context = cont;
            this.exercyse = new JSONObject(exercyse);
            this.base = new JSONObject(base);

            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColor(int color) {
        layout.setBackgroundColor(color);
    }

    private void build() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.height = 400;

        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        innerParams.setMargins(30, 20, 10, 10);
        innerParams.width = 400;

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);

        // layout.setBackgroundResource(R.drawable.metal);


        try {
            LinearLayout layout2 = new LinearLayout(context);
            layout2.setOrientation(LinearLayout.VERTICAL);
            layout2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.getLayoutParams().width = 400;

            TextView type = new TextView(context);
            type.setLayoutParams(innerParams);
            type.setTextColor(Color.parseColor("#FFFFFF"));
            type.setText(base.getString("name"));
            type.setTextSize(34);
            layout2.addView(type);

            if (!exercyse.getString("time").equals("null")) {
                TextView time = new TextView(context);
                time.setLayoutParams(innerParams);
                time.setTextColor(Color.parseColor("#FFFFFF"));
                time.setText("Time: " + exercyse.getString("time"));
                time.setTextSize(14);
                layout2.addView(time);
            }

            if (!exercyse.getString("sets").equals("null")) {
                TextView sets = new TextView(context);
                sets.setLayoutParams(innerParams);
                sets.setTextColor(Color.parseColor("#FFFFFF"));
                sets.setText("Sets: " + exercyse.getString("sets"));
                sets.setTextSize(14);
                layout2.addView(sets);
            }

            if (!exercyse.getString("reps").equals("null")) {
                TextView reps = new TextView(context);
                reps.setLayoutParams(innerParams);
                reps.setTextColor(Color.parseColor("#FFFFFF"));
                reps.setText("Reps: " + exercyse.getString("reps"));
                reps.setTextSize(14);
                layout2.addView(reps);
            }

            if (!exercyse.getString("weight").equals("null")) {
                TextView weight = new TextView(context);
                weight.setLayoutParams(innerParams);
                weight.setTextColor(Color.parseColor("#FFFFFF"));
                weight.setText("Weight: " + exercyse.getString("weight"));
                weight.setTextSize(14);
                layout2.addView(weight);
            }

            layout.addView(layout2);


            ImageView exercisePic = new ImageView(context);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(20, 20, 20, 20);
            switch (base.getString("name")) {
                case "Push Up":
                    exercisePic.setBackgroundResource(R.drawable.pushup);
                    break;
                case "Crunch":
                    exercisePic.setBackgroundResource(R.drawable.crunch);
                    break;
                case "Superman":
                    exercisePic.setBackgroundResource(R.drawable.superman);
                    break;
                case "Full Plank":
                    exercisePic.setBackgroundResource(R.drawable.fullplank);
                    break;
                case "Sit up":
                    exercisePic.setBackgroundResource(R.drawable.situp);
                    break;
            }
            exercisePic.setLayoutParams(buttonParams);
            exercisePic.getLayoutParams().width = 600;
            exercisePic.getLayoutParams().height = 300;
            layout.addView(exercisePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinearLayout getLayout() {
        return layout;
    }
}
