package com.songshare.songshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText username,password,passwordRepeat,email;
    private Button signup;
    private CheckBox agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.SignupUsername);
        password = findViewById(R.id.SignupPassword);
        passwordRepeat = findViewById(R.id.SignupPasswordRepeat);
        email = findViewById(R.id.SignupEmail);
        signup = findViewById(R.id.SignUpButton);
        agreement = findViewById(R.id.EULAAgreement);
    }

    private boolean checkFields() {
        String name = username.getText().toString();
        String pass = password.getText().toString();
        String passRepeat = passwordRepeat.getText().toString();
        String emai = email.getText().toString();

        if(name != "" && pass != "" && passRepeat != "" && pass == passRepeat && checkEmail(emai) && agreement.isChecked()){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkEmail(String email) {
        return true;
    }
}
