package workout.exercise;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseExercyseObject implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaseExercyseObject> CREATOR = new Parcelable.Creator<BaseExercyseObject>() {
        @Override
        public BaseExercyseObject createFromParcel(Parcel in) {
            return new BaseExercyseObject(in);
        }

        @Override
        public BaseExercyseObject[] newArray(int size) {
            return new BaseExercyseObject[size];
        }
    };
    private String name;
    private String description;
    private String id;
    private String imageString;
    private String howToUrl;



    /*
    All the getters and setters
     */

    BaseExercyseObject(String name, String description,
                       String imageString, String howToUrl, String id) {
        this.name = name;
        this.description = description;
        this.imageString = imageString;
        this.howToUrl = howToUrl;
        this.id = id;
    }

    protected BaseExercyseObject(Parcel in) {
        name = in.readString();
        description = in.readString();
        id = in.readString();
        imageString = in.readString();
        howToUrl = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    /*
    All the parcel stuff
     */

    public String getHowToUrl() {
        return howToUrl;
    }

    public void setHowToUrl(String howToUrl) {
        this.howToUrl = howToUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(imageString);
        dest.writeString(howToUrl);
    }
}