package com.songshare.songshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private RecyclerView tracks;
    private ViewGroup base;
    private NetworkHandler network;
    private PreferenceHandler prefs;
    private List<Track> trax;
    private SwipeRefreshLayout swipeRefresh;
    private Animation fadeIn;
    private TrackAdapter trackAdapt;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new PreferenceHandler(getContext());
        trax = new ArrayList<>();
        network = new NetworkHandler(getContext());
        getTracks();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feed_fragment, container, false);

        //Initialize the swipe to refresh layout.
        swipeRefresh = v.findViewById(R.id.ListLayout);
        base = v.findViewById(R.id.BaseLayout);
        tracks = (RecyclerView) v.findViewById(R.id.TracksView);
        tracks.setLayoutManager(new LinearLayoutManager(getContext()));
        trackAdapt = new TrackAdapter(getContext(),trax);
        tracks.setAdapter(trackAdapt);
        fadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);

        //When a user refreshes we want to empty the track list and then fill it again later
        //when the request is completed.
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTracks();
                tracks.setVisibility(View.GONE);
                trax = new ArrayList<>();
                trackAdapt.notifyDataSetChanged();
            }
        });

        return v;
    }

    //Function that makes a request to the backend to grab track shares for
    //the given user.
    private void getTracks() {
        network.getFeed(Integer.valueOf(prefs.getPreference("songshare-id")), new ResponseInterface() {
            @Override
            public void responseSuccess(JSONObject response) {
                try {
                    if(response.getString("TYPE").equals("SUCCESS")) {
                        for(int i = 0;i < response.getJSONArray("PAYLOAD").length();i++) {
                            trax.add(new Track(response.getJSONArray("PAYLOAD").getJSONObject(i)));
                        }

                        trackAdapt.notifyDataSetChanged();
                        tracks.setVisibility(View.VISIBLE);
                        tracks.startAnimation(fadeIn);
                    } else {

                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void responseError() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
