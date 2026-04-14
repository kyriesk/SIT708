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

public class SportsNewsAdapter extends RecyclerView.Adapter<SportsNewsAdapter.ViewHolder> {
    private List<SportsNews> newsList;
    private Context context;
    private OnNewsClickListener listener;

    public interface OnNewsClickListener {
        void onNewsClick(SportsNews news);
    }

    public SportsNewsAdapter(List<SportsNews> newsList, Context context, OnNewsClickListener listener) {
        this.newsList = newsList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportsNews news = newsList.get(position);
        
        int resId = context.getResources().getIdentifier(news.getImageResId(), "drawable", context.getPackageName());
        holder.newsImage.setImageResource(resId != 0 ? resId : R.drawable.ic_launcher_background);
        
        holder.newsTitle.setText(news.getTitle());
        holder.newsDescription.setText(news.getDescription());
        holder.newsCategory.setText(news.getCategory().getDisplayName());
        holder.newsAuthor.setText("By " + news.getAuthor());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNewsClick(news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateNewsList(List<SportsNews> filteredNews) {
        this.newsList = filteredNews;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
        TextView newsDescription;
        TextView newsCategory;
        TextView newsAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_item_image);
            newsTitle = itemView.findViewById(R.id.news_item_title);
            newsDescription = itemView.findViewById(R.id.news_item_description);
            newsCategory = itemView.findViewById(R.id.news_item_category);
            newsAuthor = itemView.findViewById(R.id.news_item_author);
        }
    }
}

