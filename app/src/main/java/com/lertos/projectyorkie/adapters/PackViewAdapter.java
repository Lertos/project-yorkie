package com.lertos.projectyorkie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.PackDog;

import java.util.ArrayList;
import java.util.List;

public class PackViewAdapter extends RecyclerView.Adapter<PackViewAdapter.ViewHolder> implements BindDataToView {

    private List<PackDog> packList = new ArrayList<>();

    public void setDataList(List<?> list) {
        this.packList = (List<PackDog>) list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pack_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (packList.get(position).isUnlocked()) {
            holder.tvDogName.setTextColor(ContextCompat.getColor(holder.tvDogName.getContext(), R.color.gold));

            holder.tvDogAvatar.setImageResource(packList.get(position).getAvatar());
            holder.tvDogName.setText(packList.get(position).getName());

            holder.tvDogAddedBonus.setText(
                    Helper.createSpannable(
                            "Added Bonus:",
                            " " + packList.get(position).getAddedBonus() + "%",
                            DataManager.getInstance().getPlayerData().getHighlightColor()
                    ),
                    TextView.BufferType.SPANNABLE);

        } else {
            holder.tvDogName.setTextColor(ContextCompat.getColor(holder.tvDogName.getContext(), R.color.light_gray));

            holder.tvDogAvatar.setImageResource(R.mipmap.icon_locked);
            holder.tvDogName.setText("LOCKED");
            holder.tvDogAddedBonus.setText("Win tournaments to unlock new dogs");
        }
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView tvDogAvatar;
        private final TextView tvDogName;
        private final TextView tvDogAddedBonus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDogAvatar = itemView.findViewById(R.id.ivDogAvatar);
            tvDogName = itemView.findViewById(R.id.tvDogName);
            tvDogAddedBonus = itemView.findViewById(R.id.tvDogBonus);
        }
    }

}
