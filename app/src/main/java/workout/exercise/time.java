package workout.exercise;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * a time object used for running an exercise
 * --author: Matthew Wall • ISU • SPRING 2017
 */
public class time {

    private int total; //total time in seconds
    private int fatigue; //fatigue factor
    private ExercyseObject exercyseObject; //exercise currently running
    private TextView text; //textview to display the time countdown
    private CountDownTimer cdt; //countdown object used for each exercise
    private calories c;

    /**
     * Creates a time object that keeps track of the current time of each exercise
     * -- usage: when running a workout
     */
    public time(final ExercyseObject exercyseObject, final Context context) {
        this.exercyseObject = exercyseObject;

        /**
         * creates a countdown timer based on the time of one rep * number of reps
         * measured in seconds. Easiest way to do this.
         *
         * MARK -- start the timer
         */
        cdt = new CountDownTimer(this.exercyseObject.getTime() * this.exercyseObject.getNumReps(), this.exercyseObject.getTime() * fatigue) {

            /**
             * called every tick of the Timer
             * @param millisUntilFinished - current time until finished in ms
             */
            public void onTick(long millisUntilFinished) {

                //set the text of the given textfield to

                text.setText("seconds remaining: " + millisUntilFinished / 1000);
                total += (millisUntilFinished / 1000);
                setFatigue(total);

            }

            /**
             * called when the Timer is finished
             */
            public void onFinish() {

                c = new calories(exercyseObject);
                //send the total time spent working on this given exercise to the calorie counter
                final double calories = exercyseObject.getCalories(c.getTime(), context);

                //TODO - send 'calories' back to the workoutObject or wherever to keep track of the running total calories

                //set the textfield to done or do any logic on finish of single exercise
                text.setText("done!");
                reset(); //reset everything for next exercise
            }

        }.start(); //start the timer

    }

    /**
     * sets the current fatigue of the workout (MAX == 1.5 second modifier)
     *
     * @param total - current time of the exercise complete (seconds)
     */
    private void setFatigue(int total) {

        fatigue = (total > 0 && total < 5) ? 770 :
                (total >= 5 && total < 10) ? 1000 :
                        (total >= 10 && total < 15) ? 1100 :
                                (total >= 15 && total < 25) ? 1200 :
                                        (total >= 25 && total < 30) ? 1300 :
                                                (total >= 30 && total < 45) ? 1400 :
                                                        1500; //default case modifier of a second and a half
    }

    /**
     * reset the current members
     * --usage: for when a countdown timer ends
     */
    private void reset() {
        total = 0;
        fatigue = 0;
        this.exercyseObject = null;
        cdt = null;
    }

}
