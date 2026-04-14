package com.example.istream.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istream.R;
import com.example.istream.data.entity.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    public interface OnPlaylistClickListener {
        void onItemClicked(PlaylistItem item);
    }

    private final List<PlaylistItem> items = new ArrayList<>();
    private final OnPlaylistClickListener listener;

    public PlaylistAdapter(OnPlaylistClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<PlaylistItem> playlistItems) {
        items.clear();
        items.addAll(playlistItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.textUrl.setText(item.getVideoUrl());
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private final TextView textUrl;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            textUrl = itemView.findViewById(R.id.textPlaylistUrl);
        }
    }
}

