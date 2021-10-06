package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ResetPassword extends AppCompatActivity {
    TextInputEditText  email;
    MaterialButton next;
    TextView signin;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Init();

        email.addTextChangedListener(emailValidate);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VerifyEmail(email.getText().toString())){
                    auth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Link has been sent to this email address", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ResetPassword.this, "Proccess failed! Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });
    }

    private TextWatcher emailValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String _email= email.getText().toString().trim();
            if(!VerifyEmail(_email)){
                email.setError("Invalid email");

            }else{
                email.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };


    private boolean VerifyEmail(String email){

        Pattern emailpartten = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        return  !TextUtils.isEmpty(email) && emailpartten.matcher(email).matches();

    }

    void Init() {
        email = findViewById(R.id.email);
        next = findViewById(R.id.next);
        signin = findViewById(R.id.sign_in);
    }
}