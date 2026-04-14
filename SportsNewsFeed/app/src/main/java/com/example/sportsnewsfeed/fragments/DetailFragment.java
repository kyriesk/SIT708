package com.example.sportsnewsfeed.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.adapters.RelatedStoriesAdapter;
import com.example.sportsnewsfeed.data.BookmarkRepository;
import com.example.sportsnewsfeed.data.DummyDataProvider;
import com.example.sportsnewsfeed.models.SportsNews;

import java.util.List;

public class DetailFragment extends Fragment {
    private SportsNews currentNews;
    private BookmarkRepository bookmarkRepository;
    private Button bookmarkButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bookmarkRepository = new BookmarkRepository(getContext());
        
        int newsId = getArguments() != null ? getArguments().getInt("newsId") : -1;
        
        List<SportsNews> allNews = DummyDataProvider.getSportsNews();
        for (SportsNews news : allNews) {
            if (news.getId() == newsId) {
                currentNews = news;
                break;
            }
        }
        
        if (currentNews == null) {
            return;
        }
        
        ImageView detailImage = view.findViewById(R.id.detail_image);
        TextView detailTitle = view.findViewById(R.id.detail_title);
        TextView detailMetadata = view.findViewById(R.id.detail_metadata);
        TextView detailDescription = view.findViewById(R.id.detail_description);
        bookmarkButton = view.findViewById(R.id.bookmark_button);
        RecyclerView relatedStoriesRecycler = view.findViewById(R.id.related_stories_recycler);
        
        int resId = getContext().getResources().getIdentifier(currentNews.getImageResId(), "drawable", getContext().getPackageName());
        detailImage.setImageResource(resId != 0 ? resId : R.drawable.ic_launcher_background);
        
        detailTitle.setText(currentNews.getTitle());
        detailMetadata.setText("By " + currentNews.getAuthor() + " | " + currentNews.getCategory().getDisplayName() + " | " + currentNews.getPublishDate());
        detailDescription.setText(currentNews.getDescription());
        
        updateBookmarkButton();
        
        bookmarkButton.setOnClickListener(v -> toggleBookmark());
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        relatedStoriesRecycler.setLayoutManager(layoutManager);
        List<SportsNews> relatedStories = DummyDataProvider.getRelatedStories(newsId);
        RelatedStoriesAdapter adapter = new RelatedStoriesAdapter(relatedStories, getContext(), story -> {});
        relatedStoriesRecycler.setAdapter(adapter);
    }

    private void toggleBookmark() {
        if (bookmarkRepository.isBookmarked(currentNews.getId())) {
            bookmarkRepository.removeBookmark(currentNews.getId());
        } else {
            bookmarkRepository.addBookmark(currentNews);
        }
        updateBookmarkButton();
    }

    private void updateBookmarkButton() {
        if (bookmarkRepository.isBookmarked(currentNews.getId())) {
            bookmarkButton.setText("Remove from Bookmarks");
            bookmarkButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light, null));
        } else {
            bookmarkButton.setText("Add to Bookmarks");
            bookmarkButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light, null));
        }
    }
}

