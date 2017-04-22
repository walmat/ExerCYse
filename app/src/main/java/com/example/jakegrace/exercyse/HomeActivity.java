package com.example.jakegrace.exercyse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.spotify.sdk.android.player.Player;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import community.CommunityActivity;
import kaaes.spotify.webapi.android.SpotifyService;
import loginregister.LoginActivity;
import pasta.streamer.Pasta;
import pasta.streamer.PlayerService;
import pasta.streamer.activities.MainActivity;
import pasta.streamer.utils.PreferenceUtils;
import pasta.streamer.utils.StaticUtils;
import pasta.streamer.views.Playbar;
import profile.ProfileActivity;
import reports.ReportsHomeActivity;
import staff.staffEmployerActivity;
import workout.WorkoutHome;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    @Nullable
    @Bind(R.id.playbar)
    public View playbarView;
    @Nullable
    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @Nullable
    @Bind(R.id.appbar)
    public AppBarLayout appbar;
    @Nullable
    @Bind(R.id.drawer_layout)
    public DrawerLayout drawer_layout;
    @Nullable
    @Bind(R.id.fab)
    public FloatingActionButton fab;
    @Nullable
    @Bind(R.id.content)
    public FrameLayout content;
    Button bLogout;
    SharedPreferences prefs;
    Pasta p;
    Player player;
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (SaveSharedPreference.getUserName(HomeActivity.this).length() == 0) {
            System.out.println("Something went wrong. Returning to Login. Username = " + SaveSharedPreference.getUserName(HomeActivity.this));
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            HomeActivity.this.startActivity(i);
            finish();
        } else {

            p = pasta.streamer.activities.HomeActivity.pasta;

            if (p != null) {

                p.setScreen(this);
                ButterKnife.bind(this);
                player = PlayerService.spotifyPlayer;
                Map<String, Object> limitMap = pasta.streamer.activities.HomeActivity.limitMap;
                limitMap.put(SpotifyService.LIMIT, (PreferenceUtils.getLimit(this) + 1) * 10);

                toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                if (drawer_layout != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
                }

                if (content != null) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.topMargin = StaticUtils.getStatusBarMargin(this);
                    content.setLayoutParams(layoutParams);
                }


                Playbar playbar = pasta.streamer.activities.HomeActivity.playbar;
                if (!playbar.playing) {
                    p.showToast(getString(R.string.nothing_playing));
                    if (drawer_layout != null) {
                        drawer_layout.closeDrawer(Gravity.START);
                    }

                }
                playbar.initPlayBar(playbarView);
                playbar.setPlaybarListener(new Playbar.PlaybarListener() {
                    @Override
                    public void onHide(boolean hidden) {
                        if (fab != null) {
                            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                            layoutParams.bottomMargin = hidden ? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()) : getResources().getDimensionPixelSize(R.dimen.bottom_playbar_padding);
                            fab.setLayoutParams(layoutParams);
                        }
                    }

                });


            }

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            session = new Session(getApplicationContext());

            Button bMusic = (Button) findViewById(R.id.bMusic);
            Button btnToWorkouts = (Button) findViewById(R.id.btnWorkout);
            btnToWorkouts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toWorkoutIntent = new Intent(HomeActivity.this, WorkoutHome.class);
                    HomeActivity.this.startActivity(toWorkoutIntent);

                }
            });

            Button btnToCommunity = (Button) findViewById(R.id.btnToCommunity);
            btnToCommunity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toCommunityIntent = new Intent(HomeActivity.this, CommunityActivity.class);
                    HomeActivity.this.startActivity(toCommunityIntent);
                }
            });

            Button bProfile = (Button) findViewById(R.id.bProfile);
            bProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                    HomeActivity.this.startActivity(toProfile);
                }
            });

            bLogout = (Button) findViewById(R.id.bLogout);
            bMusic.setOnClickListener(this);
            bLogout.setOnClickListener(this);

            goToStaff();
            goToReportHome();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.bLogout):
                Intent toLoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
                session.destroy();
                SaveSharedPreference.clearUserName(getApplicationContext());
                Intent i = new Intent(this, PlayerService.class);
                stopService(i);
                HomeActivity.this.startActivity(toLoginActivity);
                finish();
                break;
            case (R.id.bMusic):
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private void goToStaff() {
        Button btnStaff = (Button)findViewById(R.id.btn_staff);
        final Intent toStaff = new Intent(HomeActivity.this, staffEmployerActivity.class);

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.startActivity(toStaff);
            }
        });
    }

    private void goToReportHome() {
        Button btnReportHome = (Button) findViewById(R.id.btn_ReportHome);
        final Intent toReportHome = new Intent(HomeActivity.this, ReportsHomeActivity.class);

        btnReportHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.startActivity(toReportHome);
            }
        });
    }
}
