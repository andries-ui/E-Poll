package com.example.votingsystem.Dialog;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.votingsystem.DashBoard;
import com.example.votingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class UpdateParty extends DialogFragment {

    TextInputEditText leader_name, ID, party_name, email,city,logo, contact;
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

        view = inflater.inflate(R.layout.register_party, container, false);

           Init();

           db = FirebaseFirestore.getInstance();

           back.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dismiss();
               }
           });

           upload.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   openGallery();
               }
           });
            leader_name.addTextChangedListener(Names);
            email.addTextChangedListener(emailValidate);
            ID.addTextChangedListener(IDValidator);
            contact.addTextChangedListener(contactValidate);

           register.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (VerifyEmail(email.getText().toString().trim()) && Verifycontact(contact.getText().toString().trim())
                           && Verifyid(ID.getText().toString().trim()) && Validator()) {

                       progressBar.setVisibility(View.VISIBLE);
                       Upload(imageUri);
                   } else {
                       if (TextUtils.isEmpty(city.getText().toString())) {
                           city.setError("Invalid value provided");
                       }
                       if (TextUtils.isEmpty(leader_name.getText().toString())) {
                           leader_name.setError("Invalid value provided");
                       }

                       if (TextUtils.isEmpty(logo.getText().toString())) {
                           upload.setError("Party logo is required");
                       }

                       if (!VerifyEmail(email.getText().toString())) {
                           email.setError("Invalid value provided");
                       }
                       if (!Verifycontact(contact.getText().toString())) {
                           contact.setError("Invalid value provided");
                       }

                       if (!Verifyid(ID.getText().toString())) {
                           ID.setError("Invalid value provided");
                       }
                       if(TextUtils.isEmpty(party_name.getText().toString())){
                           party_name.setError("Invalid value provided");
                       }
                   }
               }
           });


        return view;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DashBoard.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            String scheme = imageUri.getScheme();
            if (scheme.equals("file")) {
                logo.setText(imageUri.getLastPathSegment());
            } else if (scheme.equals("content")) {
                String[] proj = {MediaStore.Images.Media.TITLE};
                Cursor cursor = getContext().getContentResolver().query(imageUri, proj, null, null, null);
                if (cursor != null && cursor.getCount() != 0) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                    cursor.moveToFirst();
                    logo.setText(cursor.getString(columnIndex));
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    boolean Validator(){

        if(TextUtils.isEmpty(city.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(leader_name.getText().toString())){
            return false;
        }

        if(TextUtils.isEmpty(logo.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(party_name.getText().toString())){
            return false;
        }


        return true;
    }

    private void Upload(Uri uri){
        if(!TextUtils.isEmpty(uri.toString())){
            FirebaseStorage.getInstance().getReference("Party").child("Party Name").child(imageUri.toString()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if(taskSnapshot.getMetadata().getReference().getDownloadUrl() != null){
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                AddData(uri.toString());
                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    void AddData(String url){

            HashMap party = new HashMap();
            party.put("leader_name",leader_name.getText().toString().trim());
            party.put("ID",ID.getText().toString().trim());
            party.put("party_name",party_name.getText().toString().trim());
            party.put("email",email.getText().toString().trim());
            party.put("city",city.getText().toString().trim());
            party.put("url",url);
            party.put("contact",contact.getText().toString().trim());
            party.put("status","active");
            party.put("nominated","null");
            db.collection("party").add(party).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(getContext(), leader_name.getText().toString().trim() + " has been registered successfully." , Toast.LENGTH_SHORT).show();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Process failed!, lease try again in a moment!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    void Init(){

        leader_name = view.findViewById(R.id.leader_names);
        ID = view.findViewById(R.id.ID);
        party_name = view.findViewById(R.id.party_name);
        email = view.findViewById(R.id.email);
        contact = view.findViewById(R.id.contact);
        city = view.findViewById(R.id.city);
        upload = view.findViewById(R.id.upload);
        logo = view.findViewById(R.id.logo);
        register = view.findViewById(R.id.register);
        back = view.findViewById(R.id.back);
        progressBar = view.findViewById(R.id.progressbar);
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

    private TextWatcher contactValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _contact= contact.getText().toString().trim();
            if(!Verifycontact(_contact)){
                contact.setError("Invalid contact");

            }else{
                contact.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
