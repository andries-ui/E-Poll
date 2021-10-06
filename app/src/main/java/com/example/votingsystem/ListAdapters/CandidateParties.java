package com.example.votingsystem.ListAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingsystem.DataClasss.PartyClass;
import com.example.votingsystem.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CandidateParties extends RecyclerView.Adapter<CandidateParties.ViewHolder> {

    private List<PartyClass> mData;
    private LayoutInflater mInflater;
    private ElectionResults.ItemClickListener mClickListener;
    Context context;

    FirebaseFirestore db;

    private static final int initial = -1;

    // data is passed into the constructor
    public CandidateParties(Context context, List<PartyClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public CandidateParties.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activate_list_item_party, parent, false);
        context = view.getContext();
        return new CandidateParties.ViewHolder(view);
    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(@NonNull CandidateParties.ViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();
        holder.partyname.setText(mData.get(position).getParty_name());
        Picasso.get().load(mData.get(position).getUrl()).error(R.drawable.ic_baseline_error_outline_24).fit().centerCrop().into(holder.logo);
        holder.status.setText(mData.get(position).getStatus());

        if(mData.get(position).getStatus().matches("disqualified")){
            holder.makeCandidate.setVisibility(View.VISIBLE);
            holder.disqualify.setVisibility(View.GONE);
        }else{
            holder.makeCandidate.setVisibility(View.GONE);
            holder.disqualify.setVisibility(View.VISIBLE);
        }


        holder.makeCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Warning!!!")
                        .setMessage("This party party will be re-instated to participate in the elections.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("party").document(mData.get(position).getKey()).update("status","active");
                            }
                        }).setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.disqualify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Warning!!!")
                        .setMessage("This party wont be nominated for the upcoming elections.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("party").document(mData.get(position).getKey()).update("status","disqualified");
                            }
                        }).setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
            notifyDataSetChanged();

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partyname, city, leaderName, leader_id, leader_more, email, ranks, votes, status;
        AppCompatImageView logo, viewDetails, makeCandidate, disqualify;

        ViewHolder(View itemView) {
            super(itemView);
            partyname = itemView.findViewById(R.id.party_name);
            city = itemView.findViewById(R.id.city);
            logo = itemView.findViewById(R.id.logo);
            viewDetails = itemView.findViewById(R.id.viewdetails);

            leader_more = itemView.findViewById(R.id.leader_more);
            leader_id = itemView.findViewById(R.id.leader_ID);
            leaderName = itemView.findViewById(R.id.leader_Names);
            email = itemView.findViewById(R.id.email);
            ranks = itemView.findViewById(R.id.ranks);
            votes = itemView.findViewById(R.id.votes);
            makeCandidate = itemView.findViewById(R.id.make_candidate);
            disqualify = itemView.findViewById(R.id.disqualify);
            status = itemView.findViewById(R.id.political_status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position

    // allows clicks events to be caught
    public void setClickListener(ElectionResults.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
