package com.example.sportsnewsfeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.models.SportsNews;

import java.util.List;

public class RelatedStoriesAdapter extends RecyclerView.Adapter<RelatedStoriesAdapter.ViewHolder> {
    private List<SportsNews> stories;
    private Context context;
    private OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(SportsNews story);
    }

    public RelatedStoriesAdapter(List<SportsNews> stories, Context context, OnStoryClickListener listener) {
        this.stories = stories;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportsNews story = stories.get(position);
        
        int resId = context.getResources().getIdentifier(story.getImageResId(), "drawable", context.getPackageName());
        holder.storyImage.setImageResource(resId != 0 ? resId : R.drawable.ic_launcher_background);
        
        holder.storyTitle.setText(story.getTitle());
        holder.storyDescription.setText(story.getDescription());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoryClick(story);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        TextView storyTitle;
        TextView storyDescription;

        ViewHolder(View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.related_story_image);
            storyTitle = itemView.findViewById(R.id.related_story_title);
            storyDescription = itemView.findViewById(R.id.related_story_description);
        }
    }
}

