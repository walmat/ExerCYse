package workout;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import workout.exercise.ExercyseObject;

public class WorkoutObject implements Parcelable {
    private ArrayList<ExercyseObject> exerciseList;
    private ArrayList<String> exIds;
    private String name;
    private String id;
    private Integer time;

    public WorkoutObject(String name, String id){
        this.name = name;
        this.exerciseList = new ArrayList<>();
        this.exIds = new ArrayList<>();
        this.id = id;
        this.time = 0;
    }

    public WorkoutObject(String name,String id, ArrayList<ExercyseObject> exList, ArrayList<String> exId){
        this.name = name;
        this.id = id;
        exerciseList = exList;
        exIds = exId;
        this.time = calcTime(exList);
    }

    private Integer calcTime(ArrayList<ExercyseObject> exerciseList){
        Integer time = 0;
        for (ExercyseObject e : exerciseList
             ) {
            time += e.getTime();
        }
        return time;
    }

    /*
    All the getters and setters
     */

    public ArrayList<ExercyseObject> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(ArrayList<ExercyseObject> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public ArrayList<String> getExIds() {
        return exIds;
    }

    public void setExIds(ArrayList<String> exIds) {
        this.exIds = exIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addEx(String id, ExercyseObject e){
        exIds.add(id);
        exerciseList.add(e);
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    /*
    All the parcel stuff
     */

    protected WorkoutObject(Parcel in) {
        if (in.readByte() == 0x01) {
            exerciseList = new ArrayList<>();
            in.readList(exerciseList, ExercyseObject.class.getClassLoader());
        } else {
            exerciseList = null;
        }
        if (in.readByte() == 0x01) {
            exIds = new ArrayList<>();
            in.readList(exIds, String.class.getClassLoader());
        } else {
            exIds = null;
        }
        name = in.readString();
        id = in.readString();
        time = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (exerciseList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(exerciseList);
        }
        if (exIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(exIds);
        }
        dest.writeString(name);
        dest.writeString(id);
        dest.writeInt(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WorkoutObject> CREATOR = new Parcelable.Creator<WorkoutObject>() {
        @Override
        public WorkoutObject createFromParcel(Parcel in) {
            return new WorkoutObject(in);
        }

        @Override
        public WorkoutObject[] newArray(int size) {
            return new WorkoutObject[size];
        }
    };
}