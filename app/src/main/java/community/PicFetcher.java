package community;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import profile.ProfileRequest;

/**
 * Created by Nate on 3/19/2017.
 */

public class PicFetcher {
    URL url = null;
    ImageView image;
    Bitmap binaryBytes;
    String username;

    public PicFetcher(ImageView img, String username) {
        try {
            this.username = username;
            image = img;
            url = new URL("http://proj-309-yt-6.cs.iastate.edu/ExerCYse/" + username + ".txt");

        } catch (Exception e) {
        }
    }

    public void run() {

        System.out.println("Begin Read");
        BufferedReader in = null;
        try {
            System.out.println("Starting connection to " + url.toString());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String base64Data = "";
            String line;
            System.out.println("Began Reading Pic data from " + url.toString());
            while ((line = in.readLine()) != null) {
                base64Data += line;
            }
            in.close();


            byte[] binaryData = Base64.decode(base64Data, Base64.DEFAULT);
            binaryBytes = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
            try {
                image.setImageBitmap(binaryBytes);
                System.out.println("Loaded Pic for " + username);
            } catch (Exception e) {
                System.out.println("Something went wrong while setting bitmap for " + username);
                e.printStackTrace();
            }
        } catch (Exception e) {
            try {
                in.close();
                System.out.println("Stream closed for " + username);
            } catch (Exception e2) {
                //System.out.println(e2);
            }
            System.out.println("Failed to load Pic " + username);
            //e.printStackTrace();
        }
    }
    public void start() {
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Fetching pic for " + username);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if ((jsonResponse.get("success").toString().equalsIgnoreCase("true"))) {
                        String data = jsonResponse.getString("data");
                        String stripped = data.replaceAll("\n", "");
                        if (!stripped.equalsIgnoreCase("null")) {
                            try {
                                byte[] decodedString = Base64.decode(stripped, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                image.setImageBitmap(bitmap);
                                System.out.println("Pic loaded");
                            } catch (Exception e) {
                                System.out.println(username + " probably has no profile pic.");
                            }
                        }
                    } else {
                        System.out.println(jsonResponse.get("success").toString() + " " + username);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("Fetching pic for " + username);
        ProfileRequest profilePicture = new ProfileRequest(username, "getProfilePicture", responseListener);
        RequestQueue queue = Volley.newRequestQueue(image.getContext());
        queue.add(profilePicture);
    }
}
