package com.lertos.projectyorkie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.model.Talent;

import java.util.ArrayList;
import java.util.List;

public class TalentsViewAdapter extends RecyclerView.Adapter<TalentsViewAdapter.ViewHolder> implements BindDataToView {

    private Context context;
    private List<Talent> talentList = new ArrayList<>();

    public TalentsViewAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<?> list) {
        this.talentList = (List<Talent>) list;
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
        holder.talentName.setText(talentList.get(position).getName());
        holder.talentDescription.setText(talentList.get(position).getDescription());
        holder.talentCurrentLevel.setText("Current Level: " + talentList.get(position).getCurrentLevel());
        //TODO: Make this a StringBuilder
        //TODO: Need to also check if the bonus is an increase or decrease
        holder.talentCurrentBonus.setText("Current Bonus: +" + talentList.get(position).getCurrentBonus() + "% (Next: +" + talentList.get(position).getNextBonus() + "%)");
        //TODO: Need to get this from a calculation in Talent class
        holder.talentNextCost.setText("456.k");
    }

    @Override
    public int getItemCount() {
        return talentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView talentName;
        private TextView talentDescription;
        private TextView talentCurrentLevel;
        private TextView talentCurrentBonus;
        private TextView talentNextCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            talentName = itemView.findViewById(R.id.talentName);
            talentDescription = itemView.findViewById(R.id.talentDescription);
            talentCurrentLevel = itemView.findViewById(R.id.talentLevel);
            talentCurrentBonus = itemView.findViewById(R.id.talentCurrentBonus);
            talentNextCost = itemView.findViewById(R.id.heartsToUpgrade);
        }
    }

}
