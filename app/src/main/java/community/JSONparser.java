package community;

import java.util.NoSuchElementException;

/**
 * Created by Nate on 2/22/2017.
 */

class JSONparser {
    //   "{'id':'001','uID':'121','name':'Bench Press','eIDs':'{001,002,003,004}'}"
    private final String origin;
    public JSONparser(String JSONstring){
        origin = JSONstring;
    }

    /**
     * Gets the string associated with the field.
     * Say the json string is "{'id':'001','uID':'121','name':'Bench Press','eIDs':'{001,002,003,004}'}"
     * And you want the name of the workout
     * call    myJSONparser.get("name");
     * and it will return "Bench Press"
     * @param fieldName
     * @return String
     */
    public String get(String fieldName)throws NoSuchElementException{
        String extract= "null";
            int indexOfField = origin.indexOf("\"" + fieldName + "\"");

            if(indexOfField == -1){
                throw new NoSuchElementException();
            }
            int indexOfVarStart = origin.substring(indexOfField).indexOf(":") + indexOfField + 2;
            int indexOfVarEnd = origin.substring(indexOfVarStart).indexOf("\"") + indexOfVarStart;
            extract = origin.substring(indexOfVarStart, indexOfVarEnd);

        return extract;
    }
    public String toString(){return origin; }
}
