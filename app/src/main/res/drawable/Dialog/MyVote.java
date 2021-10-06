package com.example.votingsystem.Dialog;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class MyVote extends DialogFragment {

    TextInputEditText leader_name, ID;
    Spinner party_List;
    ArrayList<String> list;
    TextView upload;
    ProgressBar progressBar;
    MaterialButton register;
    ImageView back;
    Uri imageUri;
    private static final int PICK_IMAGE = 100;
    View view;

    FirebaseFirestore db ;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.vote, container, false);

        try {
            Init();

            db = FirebaseFirestore.getInstance();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            leader_name.addTextChangedListener(Names);
            ID.addTextChangedListener(IDValidator);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Verifyid(ID.getText().toString().trim()) && !TextUtils.isEmpty(leader_name.getText().toString())) {

                        progressBar.setVisibility(View.VISIBLE);
                        AddData();
                    } else {

                        if (TextUtils.isEmpty(leader_name.getText().toString())) {
                            leader_name.setError("Invalid value provided");
                        }
                        if (!Verifyid(ID.getText().toString())) {
                            ID.setError("Invalid value provided");
                        }
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

                                    list.add(party_name);

                                    ArrayAdapter<String> adapter =
                                            new ArrayAdapter<>(getContext(),
                                                    android.R.layout.simple_spinner_dropdown_item, list);

                                    party_List.setAdapter(adapter);


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
        return view;
    }

    void AddData(){

        Date date = Calendar.getInstance().getTime();
        HashMap party = new HashMap();
        party.put("leader_name",leader_name.getText().toString().trim());
        party.put("ID",ID.getText().toString().trim());
        party.put("party",party_List.getSelectedItem());
        party.put("date",date.toString());
        party.put("source","E-poll");

        try {
            HashMap registra = new HashMap();
            registra.put("leader_name", leader_name.getText().toString().trim());
            registra.put("ID", ID.getText().toString().trim());
            db.collection("votes").document( ID.getText().toString().trim()).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(getContext(), "You haave made a change.!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Process failed!, lease try again in a moment!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void Init(){

        leader_name = view.findViewById(R.id.leader_names);
        ID = view.findViewById(R.id.ID);
        register = view.findViewById(R.id.register);
        back = view.findViewById(R.id.back);
        progressBar = view.findViewById(R.id.progressbar);
        party_List = view.findViewById(R.id.my_party);
    }


    private TextWatcher IDValidator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _id= ID.getText().toString().trim();
            if(!Verifyid(_id)){
                ID.setError("Invalid ID");

            }else{
                ID.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private TextWatcher Names = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _confirmPassword = leader_name.getText().toString().trim();
            if(!TextUtils.isEmpty(_confirmPassword)){
                leader_name.setError(null);
            }else{
                leader_name.setError("Invalid names");
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

    private boolean Verifycontact(String cntr){

        Pattern contactpartten = Pattern.compile("^(27|0)[6-8][0-8]{8}");

        return  !TextUtils.isEmpty(cntr) && contactpartten.matcher(cntr).matches();

    }

    private boolean Verifyid(String id){

        String IdExpression = "(((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229))(( |)(\\d{4})( |)(\\d{3})|(\\d{7}))";

        Pattern idpartten = Pattern.compile(IdExpression);

        return  !TextUtils.isEmpty(id) && idpartten.matcher(id).matches();

    }
}
