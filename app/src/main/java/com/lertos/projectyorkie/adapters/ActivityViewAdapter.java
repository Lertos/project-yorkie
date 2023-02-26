package com.lertos.projectyorkie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.IdleNumber;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewAdapter extends RecyclerView.Adapter<ActivityViewAdapter.ViewHolder> implements BindDataToView {

    private List<Activity> activityList = new ArrayList<>();
    private Toast toastMsg;

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

    private void displayNotEnoughHeartsToast(View view) {
        if (toastMsg != null)
            toastMsg.cancel();

        toastMsg = Toast.makeText(view.getContext(), "You do not have enough hearts", Toast.LENGTH_SHORT);
        toastMsg.show();
    }

    private boolean isValidUpgrade(View view, int position) {
        double upgradeCost = activityList.get(position).getUpgradeCost(-1);
        boolean canAffordUpgrade = Helper.canAffordUpgradeWithHearts(upgradeCost);

        if (!canAffordUpgrade) {
            displayNotEnoughHeartsToast(view);
            return false;
        }
        return true;
    }

    private boolean isValidUnlock(View view, int position) {
        double unlockCost = activityList.get(position).getUnlockCost();
        boolean canAffordUnlock = Helper.canAffordUpgradeWithHearts(unlockCost);

        if (!canAffordUnlock) {
            displayNotEnoughHeartsToast(view);
            return false;
        }
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Set onClick listeners
        holder.btnUpgradeMax.setOnClickListener(view -> {
            if (!isValidUpgrade(view, position))
                return;

            activityList.get(position).buyMaxLevels();

            refreshChangingData(holder, position);
        });

        holder.btnUpgradeSingle.setOnClickListener(view -> {
            if (!isValidUpgrade(view, position))
                return;

            activityList.get(position).levelUp();

            refreshChangingData(holder, position);
        });

        holder.btnUpgradeUnlock.setOnClickListener(view -> {
            if (!isValidUnlock(view, position))
                return;

            activityList.get(position).levelUp();

            refreshChangingData(holder, position);
        });

        //Update the info of the activity panel
        refreshChangingData(holder, position);

        if (!activityList.get(position).isUnlocked()) {
            //Set the data to show the locked state
            holder.tvActivityName.setText("LOCKED");
            holder.tvUpgradeCost.setText(IdleNumber.getStrNumber(activityList.get(position).getUnlockCost()));

            //Set the visibility to shrink the layout to needed items only
            holder.tvActivityLevel.setVisibility(View.GONE);
            holder.tvCurrentOutput.setVisibility(View.GONE);
            holder.btnUpgradeMax.setVisibility(View.GONE);
            holder.btnUpgradeSingle.setVisibility(View.GONE);
            holder.btnUpgradeUnlock.setVisibility(View.VISIBLE);
        }

    }

    private void refreshChangingData(ViewHolder holder, int position) {
        if (activityList.get(position).isUnlocked()) {
            holder.btnUpgradeUnlock.setVisibility(View.GONE);

            holder.tvActivityLevel.setVisibility(View.VISIBLE);
            holder.tvCurrentOutput.setVisibility(View.VISIBLE);
            holder.btnUpgradeMax.setVisibility(View.VISIBLE);
            holder.btnUpgradeSingle.setVisibility(View.VISIBLE);
        }

        holder.tvActivityName.setText(activityList.get(position).getName());

        holder.tvActivityLevel.setText(
                Helper.createSpannable(
                        "Level:",
                        " " + activityList.get(position).getCurrentLevel(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        holder.tvCurrentOutput.setText(
                Helper.createSpannable(
                        "Income:",
                        " " + IdleNumber.getStrNumber(activityList.get(position).getCurrentIncome()),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        holder.tvNextOutput.setText(
                Helper.createSpannable(
                        "Next Income:",
                        " " + IdleNumber.getStrNumber(activityList.get(position).getNextIncome()),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        holder.tvUpgradeCost.setText(IdleNumber.getStrNumber(activityList.get(position).getUpgradeCost(-1)));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvActivityName;
        private TextView tvActivityLevel;
        private TextView tvCurrentOutput;
        private TextView tvNextOutput;
        private TextView tvUpgradeCost;
        private Button btnUpgradeMax;
        private Button btnUpgradeSingle;
        private Button btnUpgradeUnlock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvActivityLevel = itemView.findViewById(R.id.tvActivityLevel);
            tvCurrentOutput = itemView.findViewById(R.id.tvCurrentOutput);
            tvNextOutput = itemView.findViewById(R.id.tvNextOutput);
            tvUpgradeCost = itemView.findViewById(R.id.tvHeartsToUpgrade);
            btnUpgradeMax = itemView.findViewById(R.id.btnUpgradeMax);
            btnUpgradeSingle = itemView.findViewById(R.id.btnUpgradeSingle);
            btnUpgradeUnlock = itemView.findViewById(R.id.btnUpgradeUnlock);

        }
    }

}
