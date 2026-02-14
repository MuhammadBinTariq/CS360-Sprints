package com.example.emotilog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * EmotionSummaryAdapter displays emotion count statistics in a ListView.
 *
 * Design Rationale:
 * - Custom adapter for displaying summary statistics
 * - Shows count, percentage, and visual progress bar for each emotion
 * - Color-codes entries to match emotion colors for consistency
 * - ViewHolder pattern for performance
 * - Displays only emotions that have been logged (count > 0)
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class EmotionSummaryAdapter extends ArrayAdapter<SummaryActivity.EmotionSummaryItem> {

    private Context context;
    private List<SummaryActivity.EmotionSummaryItem> items;
    private int totalCount;

    /**
     * Constructor for EmotionSummaryAdapter
     *
     * @param context The current context
     * @param items List of summary items to display
     * @param totalCount Total count of all emotions for percentage calculation
     */
    public EmotionSummaryAdapter(Context context, List<SummaryActivity.EmotionSummaryItem> items, int totalCount) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.totalCount = totalCount;
    }

    /**
     * ViewHolder pattern for efficient view recycling
     */
    private static class ViewHolder {
        TextView tvEmotion;
        TextView tvCount;
        TextView tvPercentage;
        ProgressBar progressBar;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_emotion_summary, parent, false);

            holder = new ViewHolder();
            holder.tvEmotion = convertView.findViewById(R.id.tv_emotion_name);
            holder.tvCount = convertView.findViewById(R.id.tv_count);
            holder.tvPercentage = convertView.findViewById(R.id.tv_percentage);
            holder.progressBar = convertView.findViewById(R.id.progress_bar);
            holder.colorIndicator = convertView.findViewById(R.id.color_indicator);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SummaryActivity.EmotionSummaryItem item = items.get(position);

        if (item != null) {
            Emotion emotion = item.getEmotion();
            int count = item.getCount();
            float percentage = item.getPercentage(totalCount);

            // Set emotion name with emoji
            holder.tvEmotion.setText(emotion.getFormattedDisplay());

            // Set count
            holder.tvCount.setText(String.valueOf(count));

            // Set percentage
            holder.tvPercentage.setText(String.format(Locale.getDefault(), "%.1f%%", percentage));

            // Set progress bar
            holder.progressBar.setMax(100);
            holder.progressBar.setProgress((int) percentage);

            // Set color indicator
            try {
                int color = Color.parseColor(emotion.getColorCode());
                holder.colorIndicator.setBackgroundColor(color);

                // Also color the progress bar
                holder.progressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
            } catch (IllegalArgumentException e) {
                holder.colorIndicator.setBackgroundColor(Color.LTGRAY);
            }
        }

        return convertView;
    }
}