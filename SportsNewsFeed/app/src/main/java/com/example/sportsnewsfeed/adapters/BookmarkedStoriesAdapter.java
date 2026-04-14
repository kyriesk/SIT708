package com.example.sportsnewsfeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.models.SportsNews;

import java.util.List;

public class BookmarkedStoriesAdapter extends RecyclerView.Adapter<BookmarkedStoriesAdapter.ViewHolder> {
    private List<SportsNews> bookmarkedStories;
    private Context context;
    private OnStoryClickListener listener;
    private OnRemoveBookmarkListener removeListener;

    public interface OnStoryClickListener {
        void onStoryClick(SportsNews story);
    }

    public interface OnRemoveBookmarkListener {
        void onRemoveBookmark(int newsId);
    }

    public BookmarkedStoriesAdapter(List<SportsNews> bookmarkedStories, Context context, 
                                   OnStoryClickListener listener, OnRemoveBookmarkListener removeListener) {
        this.bookmarkedStories = bookmarkedStories;
        this.context = context;
        this.listener = listener;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookmarked_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportsNews story = bookmarkedStories.get(position);
        
        int resId = context.getResources().getIdentifier(story.getImageResId(), "drawable", context.getPackageName());
        holder.storyImage.setImageResource(resId != 0 ? resId : R.drawable.ic_launcher_background);
        
        holder.storyTitle.setText(story.getTitle());
        holder.storyDescription.setText(story.getDescription());
        holder.storyCategory.setText(story.getCategory().getDisplayName());
        holder.storyAuthor.setText("By " + story.getAuthor());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoryClick(story);
            }
        });
        
        holder.removeButton.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemoveBookmark(story.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkedStories.size();
    }

    public void updateBookmarks(List<SportsNews> newBookmarks) {
        this.bookmarkedStories = newBookmarks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        TextView storyTitle;
        TextView storyDescription;
        TextView storyCategory;
        TextView storyAuthor;
        ImageButton removeButton;

        ViewHolder(View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.bookmarked_story_image);
            storyTitle = itemView.findViewById(R.id.bookmarked_story_title);
            storyDescription = itemView.findViewById(R.id.bookmarked_story_description);
            storyCategory = itemView.findViewById(R.id.bookmarked_story_category);
            storyAuthor = itemView.findViewById(R.id.bookmarked_story_author);
            removeButton = itemView.findViewById(R.id.remove_bookmark_button);
        }
    }
}

