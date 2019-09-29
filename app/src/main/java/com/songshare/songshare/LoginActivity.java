package com.songshare.songshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText username,password;
    private Button login,signup;
    private NetworkHandler network;
    private PreferenceHandler prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.LoginUsername);
        password = findViewById(R.id.LoginPassword);
        login = findViewById(R.id.LoginButton);
        signup = findViewById(R.id.LoginSignup);
        network = new NetworkHandler(this.getApplicationContext());
        prefs = new PreferenceHandler(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(i);
            }
        });

        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()) {
                    network.checkLogin(username.getText().toString(), password.getText().toString(), new ResponseInterface() {
                        @Override
                        public void responseSuccess(JSONObject response) {
                            try {
                                prefs.setPreference("songshare-id",String.valueOf(response.getJSONObject("PAYLOAD").getInt("_id")));
                                prefs.setPreference("songshare-username",String.valueOf(response.getJSONObject("PAYLOAD").getString("username")));
                                Intent i = new Intent(getApplicationContext(),FeedActivity.class);
                                startActivity(i);
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void responseError() {
                            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),"SFADSF",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkFields() {
        String name_value,pass_value;

        name_value = this.username.getText().toString();
        pass_value = this.password.getText().toString();

        if(name_value != "" && pass_value != "") {
            return true;
        } else {
            return false;
        }
    }
}