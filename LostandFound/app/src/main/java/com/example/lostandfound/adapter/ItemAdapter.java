package com.example.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.R;
import com.example.lostandfound.model.Item;
import com.example.lostandfound.util.DateTimeUtil;
import com.example.lostandfound.util.ImageManager;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ItemAdapter(Context context, List<Item> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.onItemClickListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.categoryTextView.setText(item.getCategoryName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.statusTextView.setText(item.getStatus());
        holder.dateTextView.setText("Posted: " + item.getDatePosted());
        holder.locationTextView.setText("Location: " + item.getLocation());

        // Load image or show placeholder
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            holder.imageView.setImageBitmap(ImageManager.loadThumbnail(item.getImagePath()));
        } else {
            holder.imageView.setBackgroundResource(R.drawable.placeholder_image);
            holder.imageView.setImageBitmap(null);
        }

        // Set status badge color and background
        if ("Lost".equals(item.getStatus())) {
            holder.statusTextView.setBackgroundResource(R.drawable.status_badge_lost);
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            holder.statusTextView.setBackgroundResource(R.drawable.status_badge_found);
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.white, null));
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView categoryTextView;
        TextView statusTextView;
        TextView descriptionTextView;
        TextView dateTextView;
        TextView locationTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
            titleTextView = itemView.findViewById(R.id.itemTitleTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
}

