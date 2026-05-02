package com.example.llmchatbot.network;

import com.google.gson.annotations.SerializedName;

public class LLMRequest {
    @SerializedName("model")
    public String model;

    @SerializedName("prompt")
    public String prompt;
    
    @SerializedName("stream")
    public boolean stream;

    public LLMRequest(String prompt) {
        this.model = "mistral";
        this.prompt = prompt;
        this.stream = false;
    }
}
