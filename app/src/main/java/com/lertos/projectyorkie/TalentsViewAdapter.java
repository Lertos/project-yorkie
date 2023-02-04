package com.lertos.projectyorkie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TalentsViewAdapter extends RecyclerView.Adapter<TalentsViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> talentList = new ArrayList<>();

    public TalentsViewAdapter(Context context) {
        this.context = context;
    }

    public void setTalentList(ArrayList<String> newTalentList) {
        this.talentList = newTalentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talent_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.talentName.setText(talentList.get(position));
    }

    @Override
    public int getItemCount() {
        return talentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView talentName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            talentName = itemView.findViewById(R.id.talentName);
        }
    }

}
