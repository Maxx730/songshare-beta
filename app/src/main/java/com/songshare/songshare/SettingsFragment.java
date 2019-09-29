package com.songshare.songshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends Fragment {

    private Button LogoutButton;
    private PreferenceHandler prefs;
    private NetworkHandler network;
    private TextView settingsUsername,settingsFollowers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new PreferenceHandler(getContext());
        network = new NetworkHandler(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        settingsUsername = v.findViewById(R.id.SettingsUsername);
        network.getUser(prefs.getPreference("songshare-id"), new ResponseInterface() {
            @Override
            public void responseSuccess(JSONObject response) {
                try {
                    settingsUsername.setText(response.getString("username"));
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void responseError() {
                Toast.makeText(getContext(),"ERROR GRABBING USER INFO",Toast.LENGTH_LONG).show();
            }
        });

        LogoutButton = v.findViewById(R.id.LogoutButton);

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.deletePreference("songshare-id");
                prefs.deletePreference("songshare-username");
                Intent i = new Intent(getContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        return v;
    }
}
