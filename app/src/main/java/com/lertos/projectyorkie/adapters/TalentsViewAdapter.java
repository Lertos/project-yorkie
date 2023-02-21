package com.lertos.projectyorkie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.IdleNumber;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.Talent;

import java.util.ArrayList;
import java.util.List;

public class TalentsViewAdapter extends RecyclerView.Adapter<TalentsViewAdapter.ViewHolder> implements BindDataToView {

    private List<Talent> talentList = new ArrayList<>();
    private Toast toastMsg;

    public TalentsViewAdapter() {}

    public void setDataList(List<?> list) {
        this.talentList = (List<Talent>) list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talent, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO: Need to also check if the bonus is an increase or decrease

        //Set onClick listeners
        holder.talentUpgradeButton.setOnClickListener(v -> {
            double upgradeCost = talentList.get(position).getNextUpgradeCost();
            boolean canAffordUpgrade = Helper.canAffordUpgradeWithHearts(upgradeCost);

            if (!canAffordUpgrade) {
                if (toastMsg != null)
                    toastMsg.cancel();

                toastMsg = Toast.makeText(v.getContext(), "You do not have enough hearts", Toast.LENGTH_SHORT);
                toastMsg.show();
                return;
            }
            talentList.get(position).levelUp();

            refreshChangingData(holder, position);
        });

        //Update the info of the activity panel
        refreshChangingData(holder, position);
    }

    private void refreshChangingData(ViewHolder holder, int position) {
        holder.talentName.setText(talentList.get(position).getName());

        holder.talentDescription.setText(talentList.get(position).getDescription());

        holder.talentCurrentLevel.setText(
                Helper.createSpannable(
                        "Current Level:",
                        " " + talentList.get(position).getCurrentLevel(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        StringBuilder currentBonusStr = new StringBuilder(" ");
        currentBonusStr.append(talentList.get(position).getBonusSignPrefix());
        currentBonusStr.append(IdleNumber.getStrNumber(talentList.get(position).getCurrentDisplayBonus()));
        currentBonusStr.append(talentList.get(position).getBonusTypeSuffix());

        holder.talentCurrentBonus.setText(
                Helper.createSpannable(
                        "Current Bonus:",
                        currentBonusStr.toString(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        StringBuilder nextBonusStr = new StringBuilder(" ");
        nextBonusStr.append(talentList.get(position).getBonusSignPrefix());
        nextBonusStr.append(IdleNumber.getStrNumber(talentList.get(position).getNextDisplayBonus()));
        nextBonusStr.append(talentList.get(position).getBonusTypeSuffix());

        holder.talentNextBonus.setText(
                Helper.createSpannable(
                        "Next Bonus:",
                        nextBonusStr.toString(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        holder.talentNextCost.setText(IdleNumber.getStrNumber(talentList.get(position).getNextUpgradeCost()));
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
        private TextView talentNextBonus;
        private TextView talentNextCost;
        private ImageView talentUpgradeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            talentName = itemView.findViewById(R.id.talentName);
            talentDescription = itemView.findViewById(R.id.talentDescription);
            talentCurrentLevel = itemView.findViewById(R.id.talentLevel);
            talentCurrentBonus = itemView.findViewById(R.id.talentCurrentBonus);
            talentNextBonus = itemView.findViewById(R.id.talentNextBonus);
            talentNextCost = itemView.findViewById(R.id.talentHeartsToUpgrade);
            talentUpgradeButton = itemView.findViewById(R.id.talentUpgradeButton);
        }
    }

}
