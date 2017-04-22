package workout.exercise;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.Session;

import community.ExercyseRequest;

public class ExercyseObject extends BaseExercyseObject {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExercyseObject> CREATOR = new Parcelable.Creator<ExercyseObject>() {
        @Override
        public ExercyseObject createFromParcel(Parcel in) {
            return new ExercyseObject(in);
        }

        @Override
        public ExercyseObject[] newArray(int size) {
            return new ExercyseObject[size];
        }
    };
    private int numReps;
    private int numSets;
    private int weight;
    private String baseId;
    private int time;
    private int cals;
    private Session session;

    /*
        All the getters and setters
    */

    public ExercyseObject(String name, String desc,
                          String howto, String img,
                          int numReps, int numSets,
                          int weight, String baseId) {
        super(name, desc, howto, img, baseId);
        this.numSets = numSets;
        this.numReps = numReps;
        this.weight = weight;
        this.baseId = baseId;
        this.time = numReps * numSets;
    }

    protected ExercyseObject(Parcel in) {
        super(in);
        numReps = in.readInt();
        numSets = in.readInt();
        weight = in.readInt();
        baseId = in.readString();
        time = in.readInt();
    }

    public int getNumReps() {
        return numReps;
    }

    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }

    public int getNumSets() {
        return numSets;
    }

    public void setNumSets(int numSets) {
        this.numSets = numSets;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public void setSession(Context context) {
        session = new Session(context);
    }

    /*
    All the parcel stuff
     */

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(numReps);
        dest.writeInt(numSets);
        dest.writeInt(weight);
        dest.writeString(baseId);
        dest.writeInt(time);
    }

    /**
     * returns the calories for a given exercise
     *
     * @param timeForExercise - time of a given exercise in seconds
     * @return number of calories for a ran exercise
     */
    public double getCalories(int timeForExercise, Context context) {

        //calculate calories based on cal/hr in database according to each exercise
        cals = -1;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    cals = Integer.parseInt(s);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        ExercyseRequest getWorkoutsRequest = new ExercyseRequest("getCalories", listener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getWorkoutsRequest);
        if (cals == -1) { //there was an error of some sort
            return cals;
        }
        return cals * (timeForExercise / 60);
    }
}