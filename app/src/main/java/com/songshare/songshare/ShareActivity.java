package com.songshare.songshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareActivity extends AppCompatActivity {
    private Intent i;
    private TextView track_title,track_artist,track_genre;
    private Button shareButton,cancelButton;
    private String spotID = "11dFghVXANMlKmJXsNCbNl";
    private NetworkHandler network;
    private PreferenceHandler prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_share);

        this.prefs = new PreferenceHandler(getApplicationContext());
        this.network = new NetworkHandler(getApplicationContext());
        track_title = findViewById(R.id.SharedTitle);
        track_artist = findViewById(R.id.SharedArtist);
        shareButton = findViewById(R.id.ShareTrackBtn);
        cancelButton = findViewById(R.id.CancelShareBtn);

        i = getIntent();

        if(i.getType() != null && Intent.ACTION_SEND.equals(i.getAction())) {
            if ("text/plain".equals(i.getType())) {
                String songData = i.getStringExtra(Intent.EXTRA_TEXT);

                if(songData.indexOf("open.spotify.com") > -1){
                    spotID = songData.split("track/")[1];
                    final int REQUEST_CODE = 1337;
                    final String REDIRECT_URI = "songshare://callback";

                    AuthenticationRequest.Builder builder =
                            new AuthenticationRequest.Builder("19e5b50c3e20453ba69db983dfd1c17f", AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
                    builder.setScopes(new String[]{"user-read-private"});
                    AuthenticationRequest request = builder.build();
                    AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1337){
            //We have made it through the Spotify authentication.
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode,data);
            final String accessToken = response.getAccessToken();

            if(response.getType() == AuthenticationResponse.Type.TOKEN) {
                this.network.getTrackData(spotID, accessToken, new ResponseInterface() {
                    @Override
                    public void responseSuccess(JSONObject response) {
                        try {
                            JSONObject newTrack = new JSONObject();
                            newTrack.put("title",response.getString("name"));
                            newTrack.put("artist",response.getJSONArray("artists").getJSONObject(0).getString("name"));
                            newTrack.put("spotify_id",response.getString("id"));
                            newTrack.put("art","TESTING");

                            final Track newShared = new Track(newTrack);

                            shareButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    network.shareTrack(newShared, Integer.parseInt(prefs.getPreference("songshare-id")), new ResponseInterface() {
                                        @Override
                                        public void responseSuccess(JSONObject response) {

                                            try {
                                                if(response.getString("TYPE").equals("SUCCESS")) {
                                                    Toast.makeText(getApplicationContext(),"TRACK SHARED!",Toast.LENGTH_LONG).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(),"ERROR SHARING TRACK!",Toast.LENGTH_LONG).show();
                                                }
                                            } catch(JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void responseError() {
                                            Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                            track_title.setText(response.getString("name"));
                            track_artist.setText(response.getJSONArray("artists").getJSONObject(0).getString("name"));

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void responseError() {

                    }
                });
            }
        }
    }
}
