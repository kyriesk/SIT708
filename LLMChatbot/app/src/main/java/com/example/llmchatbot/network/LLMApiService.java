package com.example.llmchatbot.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LLMApiService {
    // For Ollama or similar local/remote LLM
    @POST("/api/generate")
    Call<LLMResponse> generateResponse(@Body LLMRequest request);
}

