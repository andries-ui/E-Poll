package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingsystem.Dialog.ReleaseResults;
import com.example.votingsystem.Dialog.UpdateUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class Profile extends AppCompatActivity {


    TextView name, surname, ID, email, city;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    AppCompatImageView update, delete;
    ImageView back;
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Init();
            db = FirebaseFirestore.getInstance();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUser dialog = new UpdateUser();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                   new AlertDialog.Builder(Profile.this)
                            .setTitle("Warning!!!")
                            .setMessage("Your profile will be removed from our database")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Profile.this, LandingPage.class);
                                    startActivity(intent);
                                    finish();
                                    auth.getCurrentUser().delete();  }
                            }).setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });

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
                                        surname.setText(snapshot.get("student_number").toString());
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void Init() {
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        name = findViewById(R.id.fnames);
        city = findViewById(R.id.city);
        ID = findViewById(R.id.ID);
        email = findViewById(R.id.email);
        surname = findViewById(R.id.surname);
        back = findViewById(R.id.back);
    }


}