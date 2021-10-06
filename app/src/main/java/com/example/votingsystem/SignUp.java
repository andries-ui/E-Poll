package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    TextInputEditText names, id, email,city, password,retype_password, student_number ;
    MaterialButton register;
    ProgressBar progress;
    MaterialTextView hasCaps, hasSmallCaps, hasNumber, hasChar,MinLength;
    TextView signin;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user= auth.getCurrentUser();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Init();
        db = FirebaseFirestore.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email.addTextChangedListener(emailValidate);
        password.addTextChangedListener(PasswordValidator);
        retype_password.addTextChangedListener(ConfirmPasswod);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Verifypassword(password.getText().toString())
                        && Verifypassword(retype_password.getText().toString())
                        && VerifyEmail(email.getText().toString())){

                    register.setEnabled(false);
                    progress.setVisibility(View.VISIBLE);
                    auth = FirebaseAuth.getInstance();

                    auth.createUserWithEmailAndPassword(email.getText().toString().trim(),
                            password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            user = auth.getCurrentUser();
                            if(task.isSuccessful()){
                                try {
                                HashMap _user = new HashMap();
                                _user.put("name",names.getText().toString().trim() );
                                _user.put("email",email.getText().toString().trim() );
                                _user.put("city",city.getText().toString().trim() );
                                _user.put("id",id.getText().toString().trim() );
                                _user.put("student_number",student_number.getText().toString().trim() );

                                 db.collection("user" ).document(user.getUid()).set(_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull @NotNull Task<Void> task) {
                                         if(task.isSuccessful()){
                                             Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                                             startActivity(intent);
                                             finish();
                                         }else{
                                             register.setEnabled(true);
                                             user.delete();
                                         }
                                     }
                                 });
                                }catch (Exception e){
                                    user.delete();
                                    register.setEnabled(true);
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                user.delete();
                                register.setEnabled(true);
                                progress.setVisibility(View.GONE);
                                Snackbar.make(register, "registration unsuccessfully",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
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


    private TextWatcher PasswordValidator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _password= s.toString();

            if(TextUtils.isEmpty(_password)){
                password.setError(null);
                //validation helper
                hasCaps.setTextColor(Color.RED);
                hasChar.setTextColor(Color.RED);
                hasSmallCaps.setTextColor(Color.RED);
                MinLength.setTextColor(Color.RED);
            }

            char chr[] = s.toString().toCharArray();
            for(Character c:chr)
            {
                if (Character.isDigit(c)) {
                    hasNumber.setTextColor(Color.GREEN);
                }

                if (Pattern.matches("[a-z]", c.toString())) {
                    hasSmallCaps.setTextColor(Color.GREEN);
                }
                if (Pattern.matches("[A-Z]+", c.toString())) {
                    hasCaps.setTextColor(Color.GREEN);
                }
                if (Pattern.matches("(?=.*[@#$%^&*+=!])(?=\\S+$)", c.toString())) {
                    hasChar.setTextColor(Color.GREEN);
                }
                if (chr.length > 7) {
                    MinLength.setTextColor(Color.GREEN);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher ConfirmPasswod = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _confirmPassword = retype_password.getText().toString().trim();
            if(password.getText().toString().trim().matches(_confirmPassword)){
                retype_password.setError(null);
            }else{
                retype_password.setError("Password does not match");
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

    private boolean Verifypassword(String passw){

        Pattern passwordpartten = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=!])(?=\\S+$).{8,}");

        return  !TextUtils.isEmpty(passw) && passwordpartten.matcher(passw).matches();

    }

    void Init(){

        id = findViewById(R.id.ID);
        names = findViewById(R.id.fnames);
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);
        password = findViewById(R.id.password);
        retype_password = findViewById(R.id.retype_password);
        register = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        progress = findViewById(R.id.progressbar);
        student_number= findViewById(R.id.studentNumber);
        //textview
        hasChar = findViewById(R.id.passwordHasCharacter);
        hasCaps = findViewById(R.id.passwordHasCapital);
        hasSmallCaps = findViewById(R.id.passwordHasNoCapital);
        hasNumber = findViewById(R.id.passwordHasNumer);
        MinLength = findViewById(R.id.passwordHaEightCharacters);
    }
}