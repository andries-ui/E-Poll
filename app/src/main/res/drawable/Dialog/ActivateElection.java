package com.example.votingsystem.Dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingsystem.DataClasss.PartyClass;
import com.example.votingsystem.ListAdapters.CandidateParties;
import com.example.votingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActivateElection  extends DialogFragment {

    RecyclerView parties;
    ArrayList<PartyClass> list;
    int active = 0;
    int disqualified = 0;
    int total = 0;
    ImageView back;
    View view;
    TextView selected;
    CheckBox allow_all;
    CandidateParties adapter;
    FirebaseFirestore db;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activate_elections, container, false);

        try {
            Init();
            db = FirebaseFirestore.getInstance();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            allow_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Warning!!!")
                                .setMessage("All available parties will participate in the upcoming elections.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            db.collection("party").document(list.get(i).getKey()).update("status", "active");
                                        }
                                    }
                                }).setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Warning!!!")
                                .setMessage("All available parties will not participate in the upcoming elections.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            db.collection("party").document(list.get(i).getKey()).update("status", "disqualified");
                                        }
                                    }
                                }).setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
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
                            if (queryDocumentSnapshots != null) {

                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    try{
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

                                    if(status.matches("active")){
                                        active = active + 1;
                                    }else{
                                        disqualified = disqualified+1;
                                    }

                                    if(status.matches("active")){
                                        allow_all.setChecked(true);
                                    }else{

                                    }
                                    list.add(new PartyClass(key, leader_name, ID, party_name, email, city, url, contact, status, nominated));
                                }catch (Exception e){
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                adapter = new CandidateParties(getContext(), list);
                                parties.setLayoutManager(new LinearLayoutManager(getContext()));
                                parties.setAdapter(adapter);

                                selected.setText(list.size() + " Parties:   "+ active + " active:  "+ disqualified +" disqualified");


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

        return view;
    }

    void Init(){
        parties = view.findViewById(R.id.party_candidates);
        back = view.findViewById(R.id.back);
        allow_all = view.findViewById(R.id.allow_all);
        selected = view.findViewById(R.id.selected_status);
    }


}
