package com.songshare.songshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PreferenceHandler prefs;

    //Activity that will determine where to send the user based on logged in or not,
    //also used to check for permissions.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new PreferenceHandler(getApplicationContext());

        //Check if we have permission to access the internet, if not then ask to grant permissions.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1);
        } else {
            //Permission has already been granted.
            if(prefs.prefExists("songshare-id") && prefs.prefExists("songshare-username")) {
                Intent i = new Intent(this,FeedActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
                if(grantResults.length > 0) {
                    //Permission was granted do the rest of the logic.

                } else {
                    Toast.makeText(getApplicationContext(),"Songshare requires network access.",Toast.LENGTH_LONG).show();
                }
                break;
        }
        return;
    }
}
