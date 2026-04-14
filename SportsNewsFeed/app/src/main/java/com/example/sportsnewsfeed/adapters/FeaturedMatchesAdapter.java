package com.example.sportsnewsfeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sportsnewsfeed.models.FeaturedMatch;
import com.example.sportsnewsfeed.R;

import java.util.List;

public class FeaturedMatchesAdapter extends RecyclerView.Adapter<FeaturedMatchesAdapter.ViewHolder> {
    private List<FeaturedMatch> matches;
    private Context context;
    private OnMatchClickListener listener;

    public interface OnMatchClickListener {
        void onMatchClick(FeaturedMatch match);
    }

    public FeaturedMatchesAdapter(List<FeaturedMatch> matches, Context context, OnMatchClickListener listener) {
        this.matches = matches;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = new ConstraintLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(300, 200));
        layout.setBackgroundColor(0xFFE0E0E0);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeaturedMatch match = matches.get(position);
        
        // Clear previous views
        holder.container.removeAllViews();
        
        // Create ImageView
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int resId = context.getResources().getIdentifier(match.getImageResId(), "drawable", context.getPackageName());
        imageView.setImageResource(resId != 0 ? resId : R.drawable.ic_launcher_background);
        ConstraintLayout.LayoutParams imageParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        holder.container.addView(imageView, imageParams);
        
        // Create overlay with team info
        ConstraintLayout overlay = new ConstraintLayout(context);
        overlay.setBackgroundColor(0x99000000); // Semi-transparent black
        
        // Team names text
        TextView teamTextView = new TextView(context);
        teamTextView.setText(match.getTeam1() + " vs " + match.getTeam2());
        teamTextView.setTextColor(0xFFFFFFFF);
        teamTextView.setTextSize(14);
        ConstraintLayout.LayoutParams teamParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        teamParams.leftMargin = 10;
        teamParams.rightMargin = 10;
        teamParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        teamParams.bottomMargin = 10;
        teamParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        teamParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        overlay.addView(teamTextView, teamParams);
        
        ConstraintLayout.LayoutParams overlayParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        holder.container.addView(overlay, overlayParams);
        
        holder.container.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMatchClick(match);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void updateMatches(List<FeaturedMatch> newMatches) {
        this.matches = newMatches;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout container;

        ViewHolder(ConstraintLayout itemView) {
            super(itemView);
            this.container = itemView;
        }
    }
}

