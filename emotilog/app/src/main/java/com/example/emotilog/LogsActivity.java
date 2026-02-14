package com.example.emotilog;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * LogsActivity displays all emotion logs in chronological order.
 *
 * Design Rationale:
 * - Separate activity from MainActivity for clear separation of concerns
 * - Uses ListView with custom adapter for efficient display of many logs
 * - Shows logs in reverse chronological order (most recent first)
 * - Provides empty state message when no logs exist
 * - Includes back navigation and optional clear all functionality
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class LogsActivity extends AppCompatActivity {

    private EmotionLogManager logManager;
    private ListView lvLogs;
    private TextView tvEmptyState;
    private EmotionLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Emotion Logs");
        }

        // Initialize log manager
        logManager = EmotionLogManager.getInstance(this);

        // Initialize views
        initializeViews();

        // Load and display logs
        loadLogs();
    }

    /**
     * Initialize all view components
     */
    private void initializeViews() {
        lvLogs = findViewById(R.id.lv_logs);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        // Set up the empty state view
        lvLogs.setEmptyView(tvEmptyState);
    }

    /**
     * Load logs from manager and display them
     */
    private void loadLogs() {
        List<EmotionLog> logs = logManager.getAllLogs();

        if (logs.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            lvLogs.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            lvLogs.setVisibility(View.VISIBLE);

            // Create and set adapter
            adapter = new EmotionLogAdapter(this, logs);
            lvLogs.setAdapter(adapter);
        }
    }

    /**
     * Refresh the logs display
     * Called when returning from other activities or after data changes
     */
    private void refreshLogs() {
        loadLogs();
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
     * Refresh logs when activity resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshLogs();
    }
}