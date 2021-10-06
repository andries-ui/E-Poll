package com.example.votingsystem.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingsystem.DataClasss.PartyClass;
import com.example.votingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ElectionResults extends RecyclerView.Adapter<ElectionResults.ViewHolder> {

    private List<PartyClass> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    FirebaseFirestore db;
    ArrayList<String> votes;
    ArrayList<Integer> vCompare;

    private static final int  initial = -1;

    // data is passed into the constructor
    public ElectionResults(Context context, List<PartyClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.results_list_item_party, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        holder.partyname.setText(mData.get(position).getParty_name());
        Picasso.get().load(mData.get(position).getUrl()).error(R.drawable.ic_baseline_error_outline_24).fit().centerCrop().into(holder.logo);
        holder.status.setText(mData.get(position).getStatus());
        holder.lStatus.setText(String.valueOf(position));


        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("votes")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            votes = new ArrayList<>();
                            if(queryDocumentSnapshots != null) {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    String party_name = snapshot.get("party").toString();
                                    if(party_name.matches(mData.get(position).getParty_name())){
                                        votes.add(snapshot.getId());}
                                }

                                holder.votes.setText(String.valueOf(votes.size()) + " votes");

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partyname,status,votes,lStatus;
        AppCompatImageView logo, viewDetails;
        ViewHolder(View itemView) {
            super(itemView);
            partyname = itemView.findViewById(R.id.party_name);
            status = itemView.findViewById(R.id.city);
            logo = itemView.findViewById(R.id.logo);
            viewDetails = itemView.findViewById(R.id.viewdetails);
            votes = itemView.findViewById(R.id.voting);
            lStatus = itemView.findViewById(R.id.political_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());



        }
    }

    // convenience method for getting data at click position

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
