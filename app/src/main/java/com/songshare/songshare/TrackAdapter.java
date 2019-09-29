package com.songshare.songshare;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//Class that inflates the Single Track View for each track.
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private TextView title,artist,sharer,likeAmount;
    private ImageButton like,play;
    private NetworkHandler network;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private Context context;
    private LayoutInflater inflate;
    private List<Track> tracks;

    TrackAdapter(Context con,List<Track> trax) {
        this.context = con;
        this.inflate = LayoutInflater.from(this.context);
        this.tracks = trax;
        this.network = new NetworkHandler(this.context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = this.inflate.inflate(R.layout.track_fragment, parent, false);
        title = v.findViewById(R.id.TrackTitle);
        artist = v.findViewById(R.id.TrackArtist);
        sharer = v.findViewById(R.id.TrackSharer);
        like = v.findViewById(R.id.LikeTrackBtn);
        play = v.findViewById(R.id.PlayTrackBtn);
        likeAmount = v.findViewById(R.id.LikeAmount);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        title.setText(tracks.get(position).getTitle());
        artist.setText(tracks.get(position).getArtist());
        sharer.setText("Shared by " + tracks.get(position).getSharer());
        likeAmount.setText(String.valueOf(tracks.get(position).getLikes()));

        if(tracks.get(position).getHasLiked()) {
            like.setEnabled(false);
            like.setAlpha(.3f);
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { play.setColorFilter(Color.rgb(100,100,100));
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                network.likeTrack(tracks.get(position).getId(), new ResponseInterface() {
                    @Override
                    public void responseSuccess(JSONObject response) {
                        try {
                            System.out.println(response.getString("MESSAGE"));
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void responseError() {
                        System.out.println("not working");
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout dets  = holder.itemView.findViewById(R.id.ExtraDetails);
                dets.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
