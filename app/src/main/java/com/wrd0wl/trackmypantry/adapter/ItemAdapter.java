package com.wrd0wl.trackmypantry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.utils.Utilities;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemData> itemList;
    private final Context context;
    private final HandleItemClick handleItemClick;
    private boolean isLocalSearch;

    private static final int TYPE_LOCAL = 1;
    private static final int TYPE_REMOTE = 2;

    public ItemAdapter(Context context, HandleItemClick handleItemClick) {
        this.context = context;
        this.handleItemClick = handleItemClick;
    }

    public void setItemList(List<ItemData> itemList, boolean isLocalSearch){
        this.itemList = itemList;
        this.isLocalSearch = isLocalSearch;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_LOCAL){
            View view = LayoutInflater.from(context).inflate(R.layout.local_item_row, parent, false);
            return new LocalViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.remote_item_row, parent, false);
            return new RemoteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemData item = itemList.get(holder.getBindingAdapterPosition());
        if(getItemViewType(holder.getBindingAdapterPosition()) == TYPE_LOCAL){
            Glide.with(context).load(Utilities.getCategoryImageId(item.getCategory(), context)).into(((LocalViewHolder)holder).ivCategory);
            ((LocalViewHolder)holder).tvName.setText(item.getName());
            ((LocalViewHolder)holder).tvDescription.setText(item.getDescription());
            ((LocalViewHolder)holder).tvQuantity.setText(String.valueOf(item.getQuantity()));
            ((LocalViewHolder)holder).tvExpiryDate.setText(item.getExpiryDate());
            ((LocalViewHolder)holder).ivNext.setOnClickListener(v -> handleItemClick.itemFull(item));
        }
        else {
            ((RemoteViewHolder)holder).tvRemoteName.setText(item.getName());
            ((RemoteViewHolder)holder).tvRemoteDescription.setText(item.getDescription());
            ((RemoteViewHolder)holder).ivAddItem.setOnClickListener(v -> handleItemClick.itemAdd(item));
            ((RemoteViewHolder)holder).btnVote.setOnClickListener(v -> {
                int rating = (int) ((RemoteViewHolder)holder).ratingBar.getRating();
                handleItemClick.itemVote(item, rating);
            });


        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isLocalSearch){
            return TYPE_LOCAL;
        }
        else{
            return TYPE_REMOTE;
        }
    }

    public static class LocalViewHolder extends RecyclerView.ViewHolder{
        private final ImageView ivCategory;
        private final TextView tvName;
        private final TextView tvDescription;
        private final TextView tvQuantity;
        private final TextView tvExpiryDate;
        private final ImageView ivNext;

        public LocalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
            ivNext = itemView.findViewById(R.id.ivNext);
        }
    }

    public static class RemoteViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvRemoteName;
        private final TextView tvRemoteDescription;
        private final RatingBar ratingBar;
        private final Button btnVote;
        private final ImageView ivAddItem;

        public RemoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRemoteName = itemView.findViewById(R.id.tvRemoteName);
            tvRemoteDescription = itemView.findViewById(R.id.tvRemoteDescription);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnVote = itemView.findViewById(R.id.btnRemoteVote);
            ivAddItem = itemView.findViewById(R.id.ivAddItem);
        }
    }

    public interface HandleItemClick{
        void itemFull(ItemData item);
        void itemAdd(ItemData item);
        void itemVote(ItemData item, int rating);
    }
}
