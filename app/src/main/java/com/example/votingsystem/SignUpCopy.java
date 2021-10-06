package com.example.votingsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpCopy extends AppCompatActivity {
    TextInputEditText fnames, lname, ID, email, residencial_address, city,  password,retype_password ;
    MaterialButton register;
    TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Init();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void Init(){
        fnames = findViewById(R.id.fnames);
        ID = findViewById(R.id.ID);
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);
        password = findViewById(R.id.password);
        retype_password = findViewById(R.id.retype_password);
        register = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
    }
}