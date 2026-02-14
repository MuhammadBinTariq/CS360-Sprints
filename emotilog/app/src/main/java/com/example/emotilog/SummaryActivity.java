package com.example.emotilog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * SummaryActivity displays emotion count summaries for a selected date.
 *
 * Design Rationale:
 * - Provides statistical overview of emotion logs by frequency
 * - Allows date selection via DatePicker for viewing historical data
 * - Defaults to current date for immediate relevance
 * - Uses ListView to display emotion counts in descending order
 * - Shows total count for the selected date
 * - Separate activity maintains single responsibility principle
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class SummaryActivity extends AppCompatActivity {

    private EmotionLogManager logManager;
    private TextView tvSelectedDate;
    private TextView tvTotalCount;
    private ListView lvSummary;
    private Button btnSelectDate;
    private Date selectedDate;
    private EmotionSummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Emotion Summary");
        }

        // Initialize log manager
        logManager = EmotionLogManager.getInstance(this);

        // Initialize views
        initializeViews();

        // Set default date to today
        selectedDate = new Date();

        // Display summary for today
        updateSummary();
    }

    /**
     * Initialize all view components
     */
    private void initializeViews() {
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvTotalCount = findViewById(R.id.tv_total_count);
        lvSummary = findViewById(R.id.lv_summary);
        btnSelectDate = findViewById(R.id.btn_select_date);

        // Set up date selection button
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    /**
     * Show date picker dialog for selecting a date
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        selectedDate = newDate.getTime();
                        updateSummary();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    /**
     * Update the summary display for the selected date
     */
    private void updateSummary() {
        // Update selected date display
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        tvSelectedDate.setText(sdf.format(selectedDate));

        // Get summary data
        Map<Emotion, Integer> summary = logManager.getSummaryForDate(selectedDate);
        int totalCount = logManager.getTotalLogsForDate(selectedDate);

        // Update total count
        tvTotalCount.setText("Total Logs: " + totalCount);

        // Convert summary to list for adapter
        List<EmotionSummaryItem> summaryItems = new ArrayList<>();
        for (Map.Entry<Emotion, Integer> entry : summary.entrySet()) {
            if (entry.getValue() > 0) { // Only show emotions that were logged
                summaryItems.add(new EmotionSummaryItem(entry.getKey(), entry.getValue()));
            }
        }

        // Sort by count (descending)
        summaryItems.sort((a, b) -> b.getCount() - a.getCount());

        // Update ListView
        if (summaryItems.isEmpty()) {
            // Show empty state
            TextView emptyView = new TextView(this);
            emptyView.setText("No logs for this date");
            emptyView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            emptyView.setPadding(20, 50, 20, 20);
            lvSummary.setEmptyView(emptyView);
        }

        adapter = new EmotionSummaryAdapter(this, summaryItems, totalCount);
        lvSummary.setAdapter(adapter);
    }

    /**
     * Handle action bar back button
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Refresh summary when activity resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateSummary();
    }

    /**
     * Inner class to represent a summary item
     * Encapsulates emotion and its count
     */
    public static class EmotionSummaryItem {
        private Emotion emotion;
        private int count;

        public EmotionSummaryItem(Emotion emotion, int count) {
            this.emotion = emotion;
            this.count = count;
        }

        public Emotion getEmotion() {
            return emotion;
        }

        public int getCount() {
            return count;
        }

        public float getPercentage(int total) {
            if (total == 0) return 0;
            return (count * 100.0f) / total;
        }
    }
}