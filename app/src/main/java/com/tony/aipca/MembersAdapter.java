package com.tony.aipca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tony.aipca.Utils.CommitteeMembers;
import com.tony.aipca.Utils.MemberModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {

    ArrayList<MemberModel> mList;
    Context context;

    public MembersAdapter(Context context , ArrayList<MemberModel> mList){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_committeemember , parent ,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MemberModel model = mList.get(position);
        holder.membername.setText(model.getMembername());
        holder.memberposition.setText(model.getMemberposition());
        holder.membercontact.setText(model.getMemberphone());
        holder.memberlocation.setText(model.getMemberlocation());
        Picasso.get().load(model.getMemberImageUrl()).into(holder.memberProfile);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView memberProfile;
        TextView membername, memberposition, membercontact, memberlocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            membername = itemView.findViewById(R.id.memname);
            memberposition = itemView.findViewById(R.id.memposition);
            membercontact = itemView.findViewById(R.id.memcontact);
            memberlocation = itemView.findViewById(R.id.memlocation);
            memberProfile = itemView.findViewById(R.id.memprofilephoto);
        }
    }
}
