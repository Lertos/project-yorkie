package com.lertos.projectyorkie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewAdapter extends RecyclerView.Adapter<ActivityViewAdapter.ViewHolder> implements BindDataToView {

    private List<Activity> activityList = new ArrayList<>();

    public void setDataList(List<?> list) {
        this.activityList = (List<Activity>) list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (activityList.get(position).isUnlocked()) {
            holder.activityName.setText(activityList.get(position).getName());

            holder.activityLevel.setText(
                    Helper.createSpannable(
                            "Current Level:",
                            " " + activityList.get(position).getCurrentLevel(),
                            DataManager.getInstance().getPlayerData().getHighlightColor()
                    ),
                    TextView.BufferType.SPANNABLE);

            holder.activityCurrentOutput.setText(
                    Helper.createSpannable(
                            "Current Output (H/s):",
                            " " + activityList.get(position).getCurrentProductionOutput(),
                            DataManager.getInstance().getPlayerData().getHighlightColor()
                    ),
                    TextView.BufferType.SPANNABLE);

            holder.activityNextOutput.setText(
                    Helper.createSpannable(
                            "Next Output (H/s):",
                            " " + activityList.get(position).getNextProductionOutput(),
                            DataManager.getInstance().getPlayerData().getHighlightColor()
                    ),
                    TextView.BufferType.SPANNABLE);

            holder.activityUpgradeCost.setText(String.valueOf(activityList.get(position).getNextUpgradeCost()));
        } else {
            holder.activityName.setText("LOCKED");
            holder.activityLevel.setText("");
            holder.activityCurrentOutput.setText("");

            holder.activityNextOutput.setText(
                    Helper.createSpannable(
                            "Next Output (H/s):",
                            " " + activityList.get(position).getNextProductionOutput(),
                            DataManager.getInstance().getPlayerData().getHighlightColor()
                    ),
                    TextView.BufferType.SPANNABLE);

            holder.activityUpgradeCost.setText(String.valueOf(activityList.get(position).getNextUpgradeCost()));
        }
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView activityName;
        private TextView activityLevel;
        private TextView activityCurrentOutput;
        private TextView activityNextOutput;
        private TextView activityUpgradeCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            activityName = itemView.findViewById(R.id.activityName);
            activityLevel = itemView.findViewById(R.id.activityLevel);
            activityCurrentOutput = itemView.findViewById(R.id.activityCurrentOutput);
            activityNextOutput = itemView.findViewById(R.id.activityNextOutput);
            activityUpgradeCost = itemView.findViewById(R.id.activityHeartsToUpgrade);
        }
    }

}