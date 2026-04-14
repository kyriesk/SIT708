package com.example.sportsnewsfeed.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.adapters.FeaturedMatchesAdapter;
import com.example.sportsnewsfeed.adapters.SportsNewsAdapter;
import com.example.sportsnewsfeed.data.DummyDataProvider;
import com.example.sportsnewsfeed.models.FeaturedMatch;
import com.example.sportsnewsfeed.models.SportsNews;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private EditText searchBar;
    private RecyclerView featuredMatchesRecycler;
    private RecyclerView newsRecycler;
    private FeaturedMatchesAdapter matchesAdapter;
    private SportsNewsAdapter newsAdapter;
    private List<SportsNews> allNewsList;
    private List<FeaturedMatch> allMatches;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        searchBar = view.findViewById(R.id.search_bar);
        featuredMatchesRecycler = view.findViewById(R.id.featured_matches_recycler);
        newsRecycler = view.findViewById(R.id.news_recycler);
        
        allMatches = DummyDataProvider.getFeaturedMatches();
        allNewsList = DummyDataProvider.getSportsNews();
        
        LinearLayoutManager matchesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        featuredMatchesRecycler.setLayoutManager(matchesLayoutManager);
        matchesAdapter = new FeaturedMatchesAdapter(allMatches, getContext(), match -> {});
        featuredMatchesRecycler.setAdapter(matchesAdapter);
        
        new LinearSnapHelper().attachToRecyclerView(featuredMatchesRecycler);
        
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        newsRecycler.setLayoutManager(newsLayoutManager);
        newsAdapter = new SportsNewsAdapter(allNewsList, getContext(), news -> {
            Bundle bundle = new Bundle();
            bundle.putInt("newsId", news.getId());
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_detailFragment, bundle);
        });
        newsRecycler.setAdapter(newsAdapter);
        
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterNews(String query) {
        if (query.isEmpty()) {
            newsAdapter.updateNewsList(allNewsList);
            return;
        }
        
        List<SportsNews> filteredList = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (SportsNews news : allNewsList) {
            if (news.getCategory().getDisplayName().toLowerCase().contains(lowerQuery)) {
                filteredList.add(news);
            }
        }
        
        newsAdapter.updateNewsList(filteredList);
    }
}

