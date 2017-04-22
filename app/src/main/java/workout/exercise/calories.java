package workout.exercise;


public class calories {

    ExercyseObject exercyseObject; //current exercise
    private int time; //the running total of time

    /**
     * count calories based on an exercise's calories / hour
     *
     * @param exercyseObject - the exercise to count calories for
     */
    public calories(ExercyseObject exercyseObject) {
        //init members
        this.exercyseObject = exercyseObject;
        time = this.exercyseObject.getTime();
    }

    public int getTime() {
        return time;
    }

}
