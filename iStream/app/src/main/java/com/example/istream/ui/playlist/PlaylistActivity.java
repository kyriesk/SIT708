package com.example.istream.ui.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istream.MainActivity;
import com.example.istream.R;
import com.example.istream.data.db.AppDatabase;
import com.example.istream.data.entity.PlaylistItem;
import com.example.istream.session.SessionManager;
import com.example.istream.ui.home.HomeActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private SessionManager sessionManager;
    private AppDatabase database;
    private PlaylistAdapter adapter;

    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        sessionManager = new SessionManager(this);
        database = AppDatabase.getInstance(this);

        if (!sessionManager.isLoggedIn()) {
            goToEntry();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerPlaylist);
        textEmpty = findViewById(R.id.textEmptyPlaylist);
        Button buttonBack = findViewById(R.id.buttonBackToHome);
        Button buttonLogout = findViewById(R.id.buttonLogoutPlaylist);

        adapter = new PlaylistAdapter(item -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(HomeActivity.EXTRA_VIDEO_URL, item.getVideoUrl());
            startActivity(intent);
            finish();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonBack.setOnClickListener(v -> finish());
        buttonLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show();
            goToEntry();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlaylist();
    }

    private void loadPlaylist() {
        long userId = sessionManager.getUserId();
        executor.execute(() -> {
            List<PlaylistItem> items = database.playlistDao().getByUserId(userId);
            runOnUiThread(() -> {
                adapter.submitList(items);
                textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    private void goToEntry() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }
}



