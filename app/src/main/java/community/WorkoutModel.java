package community;

/**
 * Created by Nate on 2/22/2017.
 */

class WorkoutModel {
    //                            *24
    //   "{'id':'001','uID':'121','name':'Bench Press','eIDs':'{001,002,003,004}'}"
    private final String origin;
    public WorkoutModel(String workout){
        origin = workout;
    }
    public String get(String fieldName){
        int indexOfField = origin.indexOf("\'"+fieldName+"\'");
        int indexOfVarStart = origin.substring(indexOfField).indexOf(":")+indexOfField+2;
        int indexOfVarEnd = origin.substring(indexOfVarStart).indexOf("\'")+indexOfVarStart;
        String extract = origin.substring(indexOfVarStart,indexOfVarEnd);

        return extract;
    }
    public String toString(){return origin; }
}
