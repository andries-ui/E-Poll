package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.votingsystem.DataClasss.PartyClass;
import com.example.votingsystem.ListAdapters.ElectionResults;
import com.example.votingsystem.ListAdapters.Parties;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.DocumentType;

import java.util.ArrayList;

public class ManageParties extends Activity {

    RecyclerView partyList;
    ArrayList<PartyClass> list;
    Parties adapter;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parties);
        Init();

        db = FirebaseFirestore.getInstance();

//        FirebaseFirestore
//                .getInstance()
//                .collection("user").document(user.getUid())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        if(queryDocumentSnapshots != null) {
//
////                                String key = queryDocumentSnapshots.getQuery().get;
////                                String leader_name = queryDocumentSnapshots.getQuery().get("leader_name").toString();
////                                String ID = snapshot.get("ID").toString();
////                                String party_name = snapshot.get("party_name").toString();
////                                String email = snapshot.get("email").toString();
////                                String city = snapshot.get("city").toString();
////                                String url = snapshot.get("url").toString();
////                                String contact = snapshot.get("contact").toString();
////                                String status = snapshot.get("status").toString();
//                                String nominated = "snapshot.get("nominated).toString()";
//
//
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    void Init(){
        partyList = findViewById(R.id.party_candidates);
    }


}