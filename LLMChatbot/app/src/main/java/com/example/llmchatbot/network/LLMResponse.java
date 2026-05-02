package com.example.llmchatbot.network;

import com.google.gson.annotations.SerializedName;

public class LLMResponse {
    @SerializedName("response")
    public String response;
    
    @SerializedName("text")
    public String text;
    
    @SerializedName("generated_text")
    public String generatedText;
    
    public String getContent() {
        if (response != null && !response.isEmpty()) {
            return response;
        }
        if (text != null && !text.isEmpty()) {
            return text;
        }
        if (generatedText != null && !generatedText.isEmpty()) {
            return generatedText;
        }
        return "Sorry, I couldn't generate a response.";
    }
}

