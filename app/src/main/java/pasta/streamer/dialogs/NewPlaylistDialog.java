package pasta.streamer.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.async.Action;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pasta.streamer.Pasta;
import pasta.streamer.data.PlaylistListData;
import pasta.streamer.utils.ImageUtils;

public class NewPlaylistDialog extends AppCompatDialog {

    private Map<String, Object> map;
    private Pasta pasta;
    private PlaylistListData data;
    private OnCreateListener listener;
    private Toolbar toolbar;
    private Session session;
    private EditText titleView;
    private CheckBox isPublicView;

    public NewPlaylistDialog(Context context) {
        super(context, R.style.AppTheme_Dialog);
        pasta = (Pasta) context.getApplicationContext();
        session = new Session(context);
        map = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_playlist);

        session = new Session(getContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable close = ImageUtils.getVectorDrawable(getContext(), R.drawable.ic_close);
        DrawableCompat.setTint(close, Color.BLACK);
        toolbar.setNavigationIcon(close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) dismiss();
            }
        });

        toolbar.setTitle(data == null ? R.string.playlist_create : R.string.playlist_modify);

        titleView = (EditText) findViewById(R.id.playlistTitle);
        isPublicView = (CheckBox) findViewById(R.id.pub);

        if (data != null) {
            titleView.setText(data.playlistName);
            isPublicView.setChecked(data.playlistPublic);
        }

        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleView.getText().length() < 1)
                    titleView.setError(getContext().getString(R.string.no_playlist_text));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) dismiss();
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleView.getText().length() < 1) {
                    titleView.setError(getContext().getString(R.string.no_playlist_text));
                    return;
                }

                map.put("name", titleView.getText().toString());
                map.put("public", isPublicView.isChecked());

                new Action<Boolean>() {
                    @NonNull
                    @Override
                    public String id() {
                        return "modifyPlaylist";
                    }

                    @Nullable
                    @Override
                    protected Boolean run() throws InterruptedException {
                        try {
                            if (data != null)
                                pasta.spotifyService.changePlaylistDetails(pasta.me.id, data.playlistId, map);
                            else {
                                pasta.spotifyService.createPlaylist(pasta.me.id, map);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void done(@Nullable Boolean result) {
                        if (result == null || !result) {
                            pasta.onError(getContext(), "modify playlist action");
                        } else {
                            listener.onCreate();
                            if (data != null) {
                                data.playlistName = (String) map.get("name");
                                data.playlistPublic = (boolean) map.get("public");
                                storePlaylist(data.playlistName, data.playlistId);
                                System.out.println(data.playlistId);
                                System.out.println(data.playlistName);
                            }
                        }
                    }
                }.execute();

                if (isShowing()) dismiss();
            }
        });
    }

    public NewPlaylistDialog setPlaylist(@NonNull PlaylistListData data) {
        this.data = data;
        map.put("name", data.playlistName);
        map.put("public", data.playlistPublic);
        if (titleView != null) titleView.setText(data.playlistName);
        if (isPublicView != null) isPublicView.setChecked(data.playlistPublic);
        if (toolbar != null) toolbar.setTitle(R.string.playlist_modify);
        return this;
    }

    public void storePlaylist(String name, String id) {

        System.out.println(name);
        System.out.println(id);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success){
                        //do something on success
                    } else {
                        //error of some kind
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PlaylistRequest playlistRequest = new PlaylistRequest("storePlaylist", session.getUsername(), name, id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(playlistRequest);

    }

    public NewPlaylistDialog setOnCreateListener(OnCreateListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnCreateListener {
        void onCreate();
    }

}
