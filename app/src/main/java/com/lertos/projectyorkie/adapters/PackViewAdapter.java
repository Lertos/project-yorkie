package com.lertos.projectyorkie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.R;
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
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (packList.get(position).isUnlocked()) {
            holder.dogAvatar.setImageResource(packList.get(position).getAvatar());
            holder.dogName.setText(packList.get(position).getName());
            holder.dogAddedBonus.setText("Added Bonus: +" + packList.get(position).getAddedBonus() + "%");
        } else {
            holder.dogAvatar.setImageResource(R.mipmap.icon_locked);
            holder.dogName.setText("LOCKED");
            holder.dogAddedBonus.setText("Win tournaments to unlock new dogs");
        }
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView dogAvatar;
        private TextView dogName;
        private TextView dogAddedBonus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dogAvatar = itemView.findViewById(R.id.ivDogAvatar);
            dogName = itemView.findViewById(R.id.dogName);
            dogAddedBonus = itemView.findViewById(R.id.dogBonus);
        }
    }

}
