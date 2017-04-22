package pasta.streamer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jakegrace.exercyse.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pasta.streamer.adapters.ListAdapter;
import pasta.streamer.data.ListData;
import pasta.streamer.data.PlaylistListData;
import pasta.streamer.utils.PreferenceUtils;

public class OmniFragment extends Fragment {

    @Bind(R.id.progressBar)
    ProgressBar spinner;
    @Bind(R.id.recyclerView)
    RecyclerView recycler;
    @Bind(R.id.empty)
    View empty;
    private ListAdapter adapter;
    private ArrayList list;
    private GridLayoutManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, rootView);

        if (list == null) list = new ArrayList<>();
        else {
            empty.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            spinner.setVisibility(View.GONE);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        adapter = new ListAdapter(list);
        manager = new GridLayoutManager(getContext(), PreferenceUtils.getColumnNumber(metrics.widthPixels > metrics.heightPixels));
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getList().get(adapter.getItemViewType(position)) instanceof PlaylistListData)
                    return manager.getSpanCount();
                else return 1;
            }
        });

        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void addData(ListData data) {
        if (adapter != null) adapter.addListData(data);
        else {
            if (list == null) list = new ArrayList();
            list.add(data);
        }
        if (spinner != null) spinner.setVisibility(View.GONE);
        if (empty != null) empty.setVisibility(View.GONE);
    }

    public void swapData(ArrayList list) {
        this.list = list;
        if (adapter != null) adapter.setList(list);
        if (spinner != null) spinner.setVisibility(View.GONE);
        if (empty != null) empty.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
    }

    public void clear() {
        if (list != null) list.clear();
        if (adapter != null) adapter.notifyDataSetChanged();
        if (empty != null) empty.setVisibility(View.VISIBLE);
    }
}
