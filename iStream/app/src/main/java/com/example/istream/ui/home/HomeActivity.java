package com.example.istream.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istream.MainActivity;
import com.example.istream.R;
import com.example.istream.data.db.AppDatabase;
import com.example.istream.data.entity.PlaylistItem;
import com.example.istream.session.SessionManager;
import com.example.istream.ui.playlist.PlaylistActivity;
import com.example.istream.util.YoutubeUrlUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    public static final String EXTRA_VIDEO_URL = "extra_video_url";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EditText editTextVideoUrl;
    private WebView webView;
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            goToEntry();
            return;
        }

        TextView textWelcome = findViewById(R.id.textWelcome);
        editTextVideoUrl = findViewById(R.id.editTextVideoUrl);
        Button buttonPlay = findViewById(R.id.buttonPlay);
        Button buttonAdd = findViewById(R.id.buttonAddToPlaylist);
        Button buttonPlaylist = findViewById(R.id.buttonMyPlaylist);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        webView = findViewById(R.id.webViewPlayer);

        textWelcome.setText(getString(R.string.welcome_user, sessionManager.getUsername()));
        setupWebView();

        String incomingUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        if (!TextUtils.isEmpty(incomingUrl)) {
            editTextVideoUrl.setText(incomingUrl);
            playVideo(incomingUrl);
        }

        buttonPlay.setOnClickListener(v -> playVideo(editTextVideoUrl.getText().toString()));
        buttonAdd.setOnClickListener(v -> addCurrentUrlToPlaylist());
        buttonPlaylist.setOnClickListener(v -> startActivity(new Intent(this, PlaylistActivity.class)));
        buttonLogout.setOnClickListener(v -> {
            sessionManager.logout();
            goToEntry();
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
    }

    private void playVideo(String rawUrl) {
        String embedUrl = YoutubeUrlUtils.toEmbedUrl(rawUrl);
        if (TextUtils.isEmpty(embedUrl)) {
            Toast.makeText(this, R.string.error_invalid_youtube_url, Toast.LENGTH_SHORT).show();
            return;
        }

        editTextVideoUrl.setText(rawUrl.trim());
        webView.loadUrl(embedUrl);
    }

    private void addCurrentUrlToPlaylist() {
        String url = editTextVideoUrl.getText().toString().trim();
        if (TextUtils.isEmpty(YoutubeUrlUtils.toEmbedUrl(url))) {
            Toast.makeText(this, R.string.error_invalid_youtube_url, Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = sessionManager.getUserId();
        executor.execute(() -> {
            database.playlistDao().insert(new PlaylistItem(userId, url, System.currentTimeMillis()));
            runOnUiThread(() -> Toast.makeText(this, R.string.added_to_playlist, Toast.LENGTH_SHORT).show());
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
        if (webView != null) {
            webView.destroy();
        }
        executor.shutdown();
        super.onDestroy();
    }
}

