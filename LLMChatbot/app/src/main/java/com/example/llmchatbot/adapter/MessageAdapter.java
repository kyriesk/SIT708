package com.example.llmchatbot.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llmchatbot.R;
import com.example.llmchatbot.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_LLM = 1;

    private List<Message> messages;
    private String username;

    public MessageAdapter(List<Message> messages, String username) {
        this.messages = messages;
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.isFromUser ? VIEW_TYPE_USER : VIEW_TYPE_LLM;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_USER) {
            return new MessageViewHolder(inflater.inflate(R.layout.item_message_user, parent, false), viewType);
        } else {
            return new MessageViewHolder(inflater.inflate(R.layout.item_message_llm, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public void updateMessages(List<Message> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timestamp;
        private final ImageView avatar;
        private final int viewType;

        public MessageViewHolder(@NonNull android.view.View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            messageText = itemView.findViewById(R.id.message_text);
            timestamp = itemView.findViewById(R.id.message_timestamp);
            avatar = itemView.findViewById(viewType == 0 ? R.id.user_avatar : R.id.llm_avatar);
        }

        public void bind(Message message) {
            messageText.setText(message.content);
            timestamp.setText(formatTimestamp(message.timestamp));

            // Set avatar text with initials
            if (avatar != null) {
                String initials = message.isFromUser ? getInitials(message.username) : "AI";
                // You can use a library or create custom avatar view
                // For now, just set content description
                avatar.setContentDescription(initials);
            }
        }

        private String getInitials(String name) {
            if (name == null || name.isEmpty()) return "U";
            String[] parts = name.split(" ");
            StringBuilder initials = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    initials.append(part.charAt(0));
                }
            }
            return initials.toString().toUpperCase();
        }

        private String formatTimestamp(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}

