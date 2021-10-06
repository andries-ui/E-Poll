package com.example.votingsystem.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingsystem.DataClasss.PartyClass;
import com.example.votingsystem.ManageParties;
import com.example.votingsystem.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Parties  extends RecyclerView.Adapter<Parties.ViewHolder> {

    private List<PartyClass> mData;
    private LayoutInflater mInflater;
    private ElectionResults.ItemClickListener mClickListener;
    Context context;

    private  int colapseposision = -1;

    // data is passed into the constructor
    public Parties(Context context, List<PartyClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public Parties.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_party, parent, false);
        context = view.getContext();
        return new Parties.ViewHolder(view);
    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(@NonNull Parties.ViewHolder holder, int position) {

        holder.partyname.setText(mData.get(position).getParty_name());
        holder.city.setText(mData.get(position).getCity());
        holder.leaderName.setText(mData.get(position).getLeader_name());
        holder.leader_id.setText(mData.get(position).getID());
        holder.email.setText(mData.get(position).getEmail());
        Picasso.get().load(mData.get(position).getUrl()).error(R.drawable.ic_baseline_error_outline_24).fit().centerCrop().into(holder.logo);


        if (position == colapseposision) {
            holder.viewDetails.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            holder.details.setVisibility(android.view.View.VISIBLE);
        } else {
            holder.viewDetails.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            holder.details.setVisibility(android.view.View.GONE);
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partyname, city, leaderName, leader_id, leader_more, email, ranks, votes;
        AppCompatImageView logo, viewDetails;
        RelativeLayout details;

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
            details = itemView.findViewById(R.id.details);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            if (colapseposision >= 0) {
                int prev = colapseposision;
                notifyItemChanged(prev);
            }

            colapseposision = getAdapterPosition();
            notifyItemChanged(colapseposision);

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


