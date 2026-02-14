package com.example.emotilog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * EmotionLogAdapter is a custom adapter for displaying EmotionLog objects in a ListView.
 *
 * Design Rationale:
 * - Extends ArrayAdapter for easy integration with ListView
 * - Follows the ViewHolder pattern for efficient view recycling
 * - Custom layout allows for rich display of log information
 * - Color-codes each log entry based on emotion type
 * - Separates date and time for better readability
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class EmotionLogAdapter extends ArrayAdapter<EmotionLog> {

    private Context context;
    private List<EmotionLog> logs;

    /**
     * Constructor for EmotionLogAdapter
     *
     * @param context The current context
     * @param logs List of emotion logs to display
     */
    public EmotionLogAdapter(Context context, List<EmotionLog> logs) {
        super(context, 0, logs);
        this.context = context;
        this.logs = logs;
    }

    /**
     * ViewHolder pattern for efficient view recycling
     * Caches view references to avoid repeated findViewById calls
     */
    private static class ViewHolder {
        TextView tvEmotion;
        TextView tvDate;
        TextView tvTime;
        View colorIndicator;
    }

    /**
     * Get a view that displays the data at the specified position
     *
     * @param position Position of the item in the adapter's data set
     * @param convertView The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        // Check if we're reusing a view
        if (convertView == null) {
            // Inflate new view
            convertView = LayoutInflater.from(context).inflate(R.layout.item_emotion_log, parent, false);

            // Create ViewHolder
            holder = new ViewHolder();
            holder.tvEmotion = convertView.findViewById(R.id.tv_emotion);
            holder.tvDate = convertView.findViewById(R.id.tv_date);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            holder.colorIndicator = convertView.findViewById(R.id.color_indicator);

            // Store holder in the view
            convertView.setTag(holder);
        } else {
            // Reuse existing view
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current log
        EmotionLog log = logs.get(position);

        if (log != null) {
            // Set emotion text with emoji
            holder.tvEmotion.setText(log.getEmotion().getFormattedDisplay());

            // Set date
            holder.tvDate.setText(log.getFormattedDate());

            // Set time
            holder.tvTime.setText(log.getFormattedTime());

            // Set color indicator
            try {
                int color = Color.parseColor(log.getEmotion().getColorCode());
                holder.colorIndicator.setBackgroundColor(color);
            } catch (IllegalArgumentException e) {
                holder.colorIndicator.setBackgroundColor(Color.LTGRAY);
            }
        }

        return convertView;
    }

    /**
     * Update the adapter with new data
     *
     * @param newLogs New list of emotion logs
     */
    public void updateLogs(List<EmotionLog> newLogs) {
        this.logs = newLogs;
        clear();
        addAll(newLogs);
        notifyDataSetChanged();
    }
}