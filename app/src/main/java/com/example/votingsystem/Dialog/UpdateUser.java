package com.example.votingsystem.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.votingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class UpdateUser extends DialogFragment {
    TextInputEditText name, ID, email, city, student_number;
    MaterialButton register;
    ProgressBar progress;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db;
    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update, container, false);

        Init();
            db = FirebaseFirestore.getInstance();
            ID.setEnabled(false);
            email.addTextChangedListener(emailValidate);

            try {
                db.collection("user").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            try {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    if (snapshot.getId().matches(user.getUid())) {
                                        try {
                                            name.setText(snapshot.get("name").toString());
                                            email.setText(snapshot.get("email").toString());
                                            city.setText(snapshot.get("city").toString());
                                            ID.setText(snapshot.get("id").toString());
                                            student_number.setText(snapshot.get("student_number").toString());
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( VerifyEmail(email.getText().toString())) {

                        register.setEnabled(false);
                        progress.setVisibility(View.VISIBLE);
                        auth = FirebaseAuth.getInstance();


                        HashMap _user = new HashMap();

                        _user.put("name",name.getText().toString().trim() );
                        _user.put("email",email.getText().toString().trim() );
                        _user.put("city",city.getText().toString().trim() );
                        _user.put("id",ID.getText().toString().trim() );

                        db.collection("user").document(user.getUid()).update(_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } else {
                                    register.setEnabled(true);
                                    user.delete();
                                }
                            }
                        });


                    }
                }
            });

            return view;
        }

        private TextWatcher emailValidate = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String _email = email.getText().toString().trim();
                if (!VerifyEmail(_email)) {
                    email.setError("Invalid email");

                } else {
                    email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };


        private boolean VerifyEmail (String email){

            Pattern emailpartten = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

            return !TextUtils.isEmpty(email) && emailpartten.matcher(email).matches();

        }

        private boolean Verifypassword (String passw){

            Pattern passwordpartten = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=!])(?=\\S+$).{8,}");

            return !TextUtils.isEmpty(passw) && passwordpartten.matcher(passw).matches();

        }

        void Init () {
            name = view.findViewById(R.id.fnames);
            ID = view.findViewById(R.id.ID);
            email = view.findViewById(R.id.email);
            city = view.findViewById(R.id.city);
            register = view.findViewById(R.id.signup);
            progress = view.findViewById(R.id.progressbar);
            student_number = view.findViewById(R.id.studentNumber);

        }

}