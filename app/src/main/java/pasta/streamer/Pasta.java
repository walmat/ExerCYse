package pasta.streamer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakegrace.exercyse.R;
import com.example.jakegrace.exercyse.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.UserPrivate;
import pasta.streamer.data.AlbumListData;
import pasta.streamer.data.ArtistListData;
import pasta.streamer.data.PlaylistListData;
import pasta.streamer.data.TrackListData;
import pasta.streamer.dialogs.ErrorDialog;
import pasta.streamer.utils.PreferenceUtils;
import pasta.streamer.utils.StaticUtils;

public class Pasta extends Application {
    //below is where the client id is stored, which has been removed from the public repo for security reasons
    //instructions to obtain a client id are here: https://developer.spotify.com/web-api/tutorial/
    public final String CLIENT_ID = "a24c24787ca048249e484861457bea8e";
    public String token;
    public SpotifyApi spotifyApi;
    public SpotifyService spotifyService;
    public UserPrivate me;
    public ArrayList<AlbumListData> albums;
    public ArrayList<TrackListData> tracks;
    private ErrorDialog errorDialog;

    public void setScreen(Context context) {}

    public void onCriticalError(final Context context, String message) {
        if (errorDialog == null || !errorDialog.isShowing()) {

            String errorMessage = getString(R.string.error_msg);
            if (PreferenceUtils.isDebug(this))
                errorMessage += "\n\nError: " + message + "\nLocation: " + context.getClass().getName();

            errorDialog = new ErrorDialog(context).setMessage(errorMessage);
            errorDialog.show();
        }
    }

    public void onError(Context context, String message) {
        String toastMessage = getString(R.string.error);
        if (PreferenceUtils.isDebug(this))
            toastMessage += "\n\nError: " + message + "\nLocation: " + context.getClass().getName();

        showToast(toastMessage);
    }

    public void showToast(String message) {
        Toast toast = new Toast(this);

        View snackbar = LayoutInflater.from(this).inflate(R.layout.snackbar_layout, null);
        ((TextView) snackbar.findViewById(R.id.message)).setText(message);
        ViewCompat.setElevation(snackbar, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        toast.setView(snackbar);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setMargin(0, 0);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public ArrayList<AlbumListData> getFavoriteAlbums() {
        if (albums == null) {
            onCriticalError(this, "null favorite albums");
            return null;
        }
        return (ArrayList<AlbumListData>) albums.clone();
    }

    public ArrayList<TrackListData> getFavoriteTracks() {
        if (tracks == null) {
            onCriticalError(this, "null favorite tracks");
            return null;
        }
        return (ArrayList<TrackListData>) tracks.clone();
    }

    public boolean toggleFavorite(PlaylistListData data) {
        try {
            if (isFavorite(data)) {
                spotifyService.unfollowPlaylist(data.playlistOwnerId, data.playlistId);
            } else {
                spotifyService.followPlaylist(data.playlistOwnerId, data.playlistId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleFavorite(AlbumListData data) {
        try {
            if (isFavorite(data)) {
                spotifyService.removeFromMySavedAlbums(data.albumId);
                for (int i = 0; i < albums.size(); i++) {
                    if (albums.get(i).albumId.matches(data.albumId)) albums.remove(i);
                }
            } else {
                spotifyService.addToMySavedAlbums(data.albumId);
                albums.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleFavorite(TrackListData data) {
        try {
            if (isFavorite(data)) {
                spotifyService.removeFromMySavedTracks(data.trackId);
                for (int i = 0; i < tracks.size(); i++) {
                    if (tracks.get(i).trackId.matches(data.trackId)) tracks.remove(i);
                }
            } else {
                spotifyService.addToMySavedTracks(data.trackId);
                tracks.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleFavorite(ArtistListData data) {
        try {
            if (isFavorite(data)) {
                spotifyService.unfollowArtists(data.artistId);
            } else {
                spotifyService.followArtists(data.artistId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    public Boolean isFavorite(PlaylistListData data) throws InterruptedException {
        Boolean favorite = null;
        for (int i = 0; favorite == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                favorite = spotifyService.areFollowingPlaylist(data.playlistOwnerId, data.playlistId, me.id)[0];
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        return favorite;
    }

    @Nullable
    public Boolean isFavorite(AlbumListData data) {
        if (albums == null) return null;
        for (AlbumListData album : albums) {
            if (album.albumId.matches(data.albumId)) return true;
        }
        return false;
    }

    @Nullable
    public Boolean isFavorite(TrackListData data) {
        if (tracks == null) return null;
        for (TrackListData track : tracks) {
            if (track.trackId.matches(data.trackId)) return true;
        }
        return false;
    }

    @Nullable
    public Boolean isFavorite(ArtistListData data) throws InterruptedException {
        Boolean favorite = null;
        for (int i = 0; favorite == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                favorite = spotifyService.isFollowingArtists(data.artistId)[0];
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        return favorite;
    }

    @Nullable
    public ArtistListData getArtist(String id) throws InterruptedException {
        Artist a = null;
        for (int i = 0; a == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                a = spotifyService.getArtist(id);
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        if (a == null) return null;
        return new ArtistListData(a);
    }

    @Nullable
    public AlbumListData getAlbum(String id) throws InterruptedException {
        Album album = null;
        for (int i = 0; album == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                album = spotifyService.getAlbum(id);
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        if (album == null) return null;
        else return new AlbumListData(album);
    }

    @Nullable
    public ArrayList<TrackListData> getTracks(PlaylistListData data) throws InterruptedException {
        ArrayList<TrackListData> trackList = new ArrayList<>();
        Map<String, Object> options = new HashMap<>();

        for (int i = 0; i < data.tracks; i += 100) {
            Pager<PlaylistTrack> tracks = null;
            options.put(SpotifyService.OFFSET, i);
            for (int l = 0; tracks == null && l < PreferenceUtils.getRetryCount(this); l++) {
                try {
                    tracks = spotifyService.getPlaylistTracks(data.playlistOwnerId, data.playlistId, options);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                    else break;
                }
            }
            if (tracks == null) return null;

            for (PlaylistTrack track : tracks.items) {
                trackList.add(new TrackListData(track.track));
            }
        }

        return trackList;
    }

    @Nullable
    public ArrayList<TrackListData> getTracks(ArtistListData data) throws InterruptedException {
        Tracks tracks = null;
        for (int i = 0; tracks == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                tracks = spotifyService.getArtistTopTrack(data.artistId, Locale.getDefault().getCountry());
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        if (tracks == null) return null;

        ArrayList<TrackListData> trackList = new ArrayList<>();
        for (Track track : tracks.tracks) {
            trackList.add(new TrackListData(track));
        }
        return trackList;
    }

    @Nullable
    public ArrayList<TrackListData> getTracks(AlbumListData data) throws InterruptedException {
        Pager<TrackSimple> tracks = null;
        for (int i = 0; tracks == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                tracks = spotifyService.getAlbum(data.albumId).tracks;
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        if (tracks == null) return null;

        ArrayList<TrackListData> trackList = new ArrayList<>();
        for (TrackSimple track : tracks.items) {
            trackList.add(new TrackListData(track, data.albumName, data.albumId, data.albumImage, data.albumImageLarge));
        }
        return trackList;
    }

    @Nullable
    public Pager<PlaylistSimple> getMyPlaylists() throws InterruptedException {
        Pager<PlaylistSimple> playlists = null;
        for (int i = 0; playlists == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                playlists = spotifyService.getMyPlaylists();
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        return playlists;
    }

    @Nullable
    public ArrayList<PlaylistListData> searchPlaylists(String query, Map<String, Object> limitMap) throws InterruptedException {
        PlaylistsPager playlists = null;
        for (int i = 0; playlists == null && i < PreferenceUtils.getRetryCount(this); i++) {
            try {
                playlists = spotifyService.searchPlaylists(query, limitMap);
            } catch (Exception e) {
                e.printStackTrace();
                if (StaticUtils.shouldResendRequest(e)) Thread.sleep(200);
                else break;
            }
        }
        if (playlists == null) return null;

        ArrayList<PlaylistListData> playlistList = new ArrayList<>();
        for (PlaylistSimple playlist : playlists.playlists.items) {
            playlistList.add(new PlaylistListData(playlist, me));
        }
        return playlistList;
    }
}
