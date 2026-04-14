package com.example.sportsnewsfeed.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.adapters.BookmarkedStoriesAdapter;
import com.example.sportsnewsfeed.data.BookmarkRepository;
import com.example.sportsnewsfeed.models.SportsNews;

import java.util.List;

public class BookmarkFragment extends Fragment {
    private BookmarkRepository bookmarkRepository;
    private RecyclerView bookmarksRecycler;
    private TextView emptyMessage;
    private BookmarkedStoriesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bookmarkRepository = new BookmarkRepository(getContext());
        bookmarksRecycler = view.findViewById(R.id.bookmarks_recycler);
        emptyMessage = view.findViewById(R.id.empty_bookmarks_message);
        
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh bookmarks when returning to this fragment
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<SportsNews> bookmarks = bookmarkRepository.getAllBookmarks();
        
        if (bookmarks.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
            bookmarksRecycler.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            bookmarksRecycler.setVisibility(View.VISIBLE);
            
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            bookmarksRecycler.setLayoutManager(layoutManager);
            
            adapter = new BookmarkedStoriesAdapter(
                bookmarks,
                getContext(),
                story -> {},
                newsId -> {
                    bookmarkRepository.removeBookmark(newsId);
                    setupRecyclerView();
                }
            );
            bookmarksRecycler.setAdapter(adapter);
        }
    }
}

