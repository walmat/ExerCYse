package pasta.streamer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jakegrace.exercyse.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import pasta.streamer.adapters.FavoritePagerAdapter;
import pasta.streamer.dialogs.NewPlaylistDialog;

public class FavoritesFragment extends FabFragment {

    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.tl)
    TabLayout tl;

    private FavoritePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, rootView);

        ViewCompat.setElevation(tl, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        adapter = new FavoritePagerAdapter(getActivity(), getActivity().getSupportFragmentManager());
        vp.setAdapter(adapter);
        tl.setupWithViewPager(vp);

        setFab(true, R.drawable.ic_add, getFabClickListener());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnPageChange(R.id.vp)
    public void onPageSelected(int position) {
        if (position == 0) setFab(true, R.drawable.ic_add, getFabClickListener());
        else setFab(false, R.drawable.ic_add, getFabClickListener());
    }

    public View.OnClickListener getFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewPlaylistDialog(getContext()).setOnCreateListener(new NewPlaylistDialog.OnCreateListener() {
                    @Override
                    public void onCreate() {
                        adapter.load();
                    }
                }).show();
            }
        };
    }
}
