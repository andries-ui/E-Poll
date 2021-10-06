package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.votingsystem.Dialog.MyVote;
import com.example.votingsystem.Dialog.ReleaseResults;
import com.example.votingsystem.SliderPackage.The_Slide_Items_Model_Class;
import com.example.votingsystem.SliderPackage.The_Slide_items_Pager_Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity {
    private List<The_Slide_Items_Model_Class> listItems;
    private ViewPager page;
    private TabLayout tabLayout;

    MaterialCardView register_new_party, manage_parties,results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Init();

        tabLayout.setupWithViewPager(page,true);

        register_new_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVote dialog = new MyVote();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        manage_parties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);

            }
        });
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReleaseResults dialog = new ReleaseResults();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");

            }
        });

        FirebaseFirestore
                .getInstance()
                .collection("party")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        listItems = new ArrayList<>() ;
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

                                listItems.add(new The_Slide_Items_Model_Class(url,party_name));

                            }

                            The_Slide_items_Pager_Adapter itemsPager_adapter = new The_Slide_items_Pager_Adapter(DashBoard.this, listItems);
                            page.setAdapter(itemsPager_adapter);


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    void Init(){
        page = findViewById(R.id.my_pager) ;
        tabLayout = findViewById(R.id.my_tablayout);
        register_new_party = findViewById(R.id.register_party) ;
        manage_parties = findViewById(R.id.manage_parties);
        results = findViewById(R.id.results);
    }
}