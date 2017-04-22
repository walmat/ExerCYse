package profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;
import com.mvc.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Profile code for given user ID
 */
public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    /**
     * globals
     */
    String user;
    String id;
    boolean uploaded = false;
    EditText etUsername;
    ImageView profile_picture;
    private Session session;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        session = new Session(getApplicationContext());
        ImagePicker.setMinQuality(50, 50);
        user = session.getUsername();
        id = session.getID();
        etUsername = (EditText) findViewById(R.id.editUsername);

        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final EditText etEmail = (EditText) findViewById(R.id.editEmail);
        final EditText etAge = (EditText) findViewById(R.id.editAge);
        final EditText etBio = (EditText) findViewById(R.id.etBio);
        final TextView tvType = (TextView) findViewById(R.id.tvType);
        final EditText etWeight = (EditText) findViewById(R.id.editWeight);
        final EditText etHeight = (EditText) findViewById(R.id.editHeight);
        final LinearLayout background = (LinearLayout) findViewById(R.id.background);
        final TextView editUsername = (TextView) findViewById(R.id.editUsername);
        profile_picture = (ImageView) findViewById(R.id.profile_image);


        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    HashMap<String, String> results = new HashMap<>();
                    String fields[] = {"type","username","firstname","lastname","email","age",
                            "imgPath","bio","weight","height", "color"};

                    //put values into a Hashmap to access
                    for (String s: fields){
                        results.put(s, jsonResponse.getString(s));
                    }

                    String type = (results.get("type").equals("1")) ? "student" :
                            (results.get("type").equals("2")) ? "admin" : "employer";

                    tvType.setText(type);
                    editUsername.setText(results.get("username"));
                    tvName.setText(results.get("firstname") + " " + results.get("lastname"));
                    etEmail.setText(results.get("email"));

                    //these all can be null initially!!
                    if (!results.get("imgPath").equalsIgnoreCase("null")) {
                        getProfilePicture();
                    }
                    if (!results.get("age").equalsIgnoreCase("null")){
                        etAge.setText(results.get("age"));
                    }
                    if (!results.get("bio").equalsIgnoreCase("null")){
                        etBio.setText(results.get("bio"));
                    }
                    if (!results.get("weight").equalsIgnoreCase("null")){
                        etWeight.setText(results.get("weight"));
                    }
                    if (!results.get("height").equalsIgnoreCase("null")){
                        etHeight.setText(results.get("height"));
                    }
                    //set color of background to user preferred color
                    setColor(background, results.get("color"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptChangeColor(background);
            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptChangeUsername();
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });

        ProfileRequest profileRequest = new ProfileRequest(id, "getUserProfile", responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(profileRequest);

    }

    public void onPickImage(View view) {
        ImagePicker.pickImage(this, "Select your image:");
    }

    /**
     * A method that parses a color integer based on a given string.
     * @param name name of the color to be parsed
     * @return integer value of the name of the color
     */
    private int parseColor(String name){

        //integer values for differing color values
        return name.equalsIgnoreCase("Blue") ? -16749658 :
                name.equalsIgnoreCase("Green") ? -9202818 :
                        name.equalsIgnoreCase("Orange") ? -1213126 :
                                name.equalsIgnoreCase("Purple") ? -8496485 :
                                        name.equalsIgnoreCase("Yellow") ? -1850009 :
                                                name.equalsIgnoreCase("Pink") ? -1001549 :
                                                        name.equalsIgnoreCase("Red") ? -8712930 : -7500403; //default to grey

    }

    private void getProfilePicture() {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (!(jsonResponse.get("success").toString().equalsIgnoreCase("true"))){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Couldn't fetch profile picture. Try again!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                    else {
                        String data = jsonResponse.getString("data");
                        String stripped = data.replaceAll("\n", "");
                        if (!stripped.equalsIgnoreCase("null")) {
                            byte[] decodedString = Base64.decode(stripped, Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            profile_picture.setImageBitmap(bitmap);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProfileRequest profilePicture = new ProfileRequest(user, "getProfilePicture", responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(profilePicture);

    }

    /**
     * A method that is used to set the color of a given linearLayout.
     * @param linearLayout current linear layout to set the color of
     * @param colorName the name of the color to set the background to
     */
    private void setColor(LinearLayout linearLayout, String colorName) {

        linearLayout.setBackgroundColor(parseColor(colorName));
        linearLayout.refreshDrawableState();
        linearLayout.invalidate();
    }

    /**
     * Stores the new value of color in the database
     * @param color string of the name of the color to store
     */
    private void storeColor(String color) {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (!(jsonResponse.get("success").toString().equalsIgnoreCase("true"))){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Couldn't store changes. Try again!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProfileRequest storeColor = new ProfileRequest(user, color, "storeColor", responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(storeColor);
    }

    /**
     * Stores a change in username given by the user
     * @param username new value of username to store in the database
     */
    private void storeUsername(final String username) {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (!(jsonResponse.get("success").toString().equalsIgnoreCase("true"))){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Couldn't store changes. Try again!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                    else {
                        etUsername.setText(username);
                        user = username;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProfileRequest storeUsername = new ProfileRequest(user, username, "storeUsername", responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(storeUsername);
    }

    /**
     * Prompts the user with options to change their background color
     * @param linearLayout the linearLayout to display the change in color.
     */
    private void promptChangeColor(final LinearLayout linearLayout) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Change Color");
        b.setCancelable(true);
        final String[] choices = {"Blue", "Green", "Orange", "Purple", "Yellow", "Pink", "Red", "Cancel"};
        b.setItems(choices,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        setColor(linearLayout, choices[which]);
                        storeColor(choices[which]);
                        dialog.dismiss();
                        break;
                    case 7:
                        dialog.dismiss();
                        break;
                }
            }
        });
        b.show();
    }

    /**
     * changes the username for a given user
     */
    private void promptChangeUsername() {

        if (user.equalsIgnoreCase(session.getUsername())) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);

            final EditText input = new EditText(this);
            b.setTitle("Change username");
            input.setHeight(120);
            input.setWidth(340);
            input.setGravity(Gravity.START);
            b.setView(input);
            b.setCancelable(true);
            input.setImeOptions(EditorInfo.IME_ACTION_DONE);

            DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent KEvent) {
                    int keyaction = KEvent.getAction();

                    if (keyaction == KeyEvent.ACTION_DOWN) {
                        int keycode = KEvent.getKeyCode();
                        if (keycode == KeyEvent.KEYCODE_ENTER) {
                            //dismiss the dialog
                            dialog.dismiss();
                            if (!input.getText().toString().equalsIgnoreCase("") || input.getText().toString().length() < 5) {
                                storeUsername(input.getText().toString());
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                builder.setMessage("Username too short. Try again!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }
                    }
                    return false;
                }
            };
            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (!input.getText().toString().equalsIgnoreCase("") || input.getText().toString().length() < 5) {
                        storeUsername(input.getText().toString());
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Username too short. Try again!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }

            });
            b.setOnKeyListener(keylistener);
            b.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                if (data != null) {
                    bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

                    if (uploadImage()) {
                        profile_picture.setImageBitmap(bitmap);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    private boolean uploadImage(){

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (!(jsonResponse.get("success").toString().equalsIgnoreCase("true"))){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Couldn't store changes. Try again!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                        uploaded = false;
                    }
                    else {
                        uploaded = true;
                        getProfilePicture();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProfileRequest storeUsername = new ProfileRequest(user, getStringImage(bitmap), "storePicture", responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(storeUsername);
        return uploaded;
    }
}