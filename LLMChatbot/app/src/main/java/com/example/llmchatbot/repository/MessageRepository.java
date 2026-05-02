package com.example.llmchatbot.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.llmchatbot.database.AppDatabase;
import com.example.llmchatbot.database.MessageDao;
import com.example.llmchatbot.model.Message;
import com.example.llmchatbot.network.LLMApiService;
import com.example.llmchatbot.network.LLMRequest;
import com.example.llmchatbot.network.LLMResponse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageRepository {
    private static final String TAG = "MessageRepository";

    private static final String LLM_BASE_URL = "http://10.0.2.2:11434/";

    private final MessageDao messageDao;
    private final LLMApiService llmApiService;
    private final ExecutorService executorService;

    public MessageRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.messageDao = db.messageDao();
        this.llmApiService = createLLMService();
        this.executorService = Executors.newFixedThreadPool(2);
    }

    private LLMApiService createLLMService() {
        // Create OkHttp client with longer timeouts for LLM responses
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
                .readTimeout(60, TimeUnit.SECONDS)      // Read timeout (LLM can take 30-50 seconds)
                .writeTimeout(30, TimeUnit.SECONDS)     // Write timeout
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LLM_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(LLMApiService.class);
    }

    public LiveData<List<Message>> getMessagesByUsername(String username) {
        return messageDao.getMessagesByUsername(username);
    }

    public void insertMessage(Message message) {
        executorService.execute(() -> messageDao.insertMessage(message));
    }

    public void sendMessage(String userMessage, String username, LLMResponseCallback callback) {
        // First, save user message to database
        Message userMsg = new Message(userMessage, true, System.currentTimeMillis(), username);
        insertMessage(userMsg);

        // Real LLM API call - with debug logging
        LLMRequest request = new LLMRequest(userMessage);

        Log.d(TAG, "Sending to LLM at: " + LLM_BASE_URL);
        Log.d(TAG, "Message: " + userMessage);

        llmApiService.generateResponse(request).enqueue(new Callback<LLMResponse>() {
            @Override
            public void onResponse(Call<LLMResponse> call, Response<LLMResponse> response) {
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    String llmResponse = response.body().getContent();
                    Log.d(TAG, "LLM Response: " + llmResponse);
                    Message llmMsg = new Message(llmResponse, false, System.currentTimeMillis(), username);
                    insertMessage(llmMsg);
                    callback.onSuccess(llmResponse);
                } else {
                    Log.e(TAG, "Response not successful. Code: " + response.code());
                    handleError(callback);
                }
            }

            @Override
            public void onFailure(Call<LLMResponse> call, Throwable t) {
                Log.e(TAG, "LLM API call failed: " + t.getMessage(), t);
                t.printStackTrace();
                handleError(callback);
            }
        });
    }

    private void handleError(LLMResponseCallback callback) {
        String errorMsg = "I'm having trouble thinking right now. Please try again.";
        callback.onError(errorMsg);
    }

    public void clearMessages(String username) {
        executorService.execute(() -> messageDao.deleteMessagesByUsername(username));
    }

    public interface LLMResponseCallback {
        void onSuccess(String response);
        void onError(String error);
    }
}
