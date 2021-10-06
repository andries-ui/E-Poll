package com.example.votingsystem.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingsystem.DataClasss.PartyClass;
import com.example.votingsystem.ListAdapters.ElectionResults;
import com.example.votingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReleaseResults  extends DialogFragment {

    RecyclerView parties;
    ImageView back;
    ArrayList<PartyClass> list;
    ElectionResults adapter;
    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.release_results, container, false);
        Init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        FirebaseFirestore
                .getInstance()
                .collection("party")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        list = new ArrayList<>();
                        if(queryDocumentSnapshots != null) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                String key = snapshot.getId();
                                String leader_name = snapshot.get("leader_name").toString();
                                String ID = snapshot.get("ID").toString();
                                String party_name = snapshot.get("party_name").toString();
                                String email = snapshot.get("email").toString();
                                String city = snapshot.get("city").toString();
                                String url = snapshot.get("url").toString();
                                String contact = snapshot.get("contact").toString();
                                String status = snapshot.get("status").toString();
                                String nominated = snapshot.get("nominated").toString();

                                Toast.makeText(getContext(), party_name, Toast.LENGTH_SHORT).show();
                                list.add( new PartyClass( key,  leader_name,  ID,  party_name,  email,  city,  url,  contact,  status,  nominated));
                            }adapter = new ElectionResults(getContext(), list);
                            parties.setLayoutManager(new LinearLayoutManager(getContext()));
                            parties.setAdapter(adapter);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



       

        return view;
    }

    void Init(){
        parties = view.findViewById(R.id.party_candidates);
        back = view.findViewById(R.id.back);
    }
}
