package com.example.emotilog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * MainActivity is the main screen displaying emotion buttons for quick logging.
 *
 * Design Rationale:
 * - Provides immediate access to emotion logging via large, visible buttons
 * - Each button is color-coded based on the emotion for better UX
 * - Shows total log count to give user feedback on their logging activity
 * - Uses FloatingActionButton for navigation to logs and summary (Material Design)
 * - Follows Android lifecycle best practices by refreshing data in onResume
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private EmotionLogManager logManager;
    private TextView tvLogCount;
    private Button[] emotionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the log manager
        logManager = EmotionLogManager.getInstance(this);

        // Initialize UI components
        initializeViews();

        // Set up emotion buttons
        setupEmotionButtons();

        // Set up navigation buttons
        setupNavigationButtons();

        // Update the log count display
        updateLogCount();
    }

    /**
     * Initialize all view components
     */
    private void initializeViews() {
        tvLogCount = findViewById(R.id.tv_log_count);

        // Initialize emotion buttons array
        emotionButtons = new Button[]{
                findViewById(R.id.btn_happy),
                findViewById(R.id.btn_sad),
                findViewById(R.id.btn_grateful),
                findViewById(R.id.btn_angry),
                findViewById(R.id.btn_excited),
                findViewById(R.id.btn_anxious),
                findViewById(R.id.btn_calm),
                findViewById(R.id.btn_tired),
                findViewById(R.id.btn_surprised)
        };
    }

    /**
     * Set up click listeners for all emotion buttons
     * Each button logs the corresponding emotion and provides visual feedback
     */
    private void setupEmotionButtons() {
        Emotion[] emotions = Emotion.values();

        for (int i = 0; i < emotionButtons.length && i < emotions.length; i++) {
            final Emotion emotion = emotions[i];
            final Button button = emotionButtons[i];

            // Set button text with emoji
            button.setText(emotion.getFormattedDisplay());

            // Set button color
            try {
                button.setBackgroundColor(Color.parseColor(emotion.getColorCode()));
            } catch (IllegalArgumentException e) {
                // Use default color if parsing fails
                button.setBackgroundColor(Color.LTGRAY);
            }

            // Set click listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logEmotion(emotion);
                }
            });
        }
    }

    /**
     * Set up navigation buttons (FABs) for viewing logs and summary
     */
    private void setupNavigationButtons() {
        FloatingActionButton fabViewLogs = findViewById(R.id.fab_view_logs);
        FloatingActionButton fabViewSummary = findViewById(R.id.fab_view_summary);

        fabViewLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogs();
            }
        });

        fabViewSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSummary();
            }
        });
    }

    /**
     * Log an emotion and provide user feedback
     *
     * @param emotion The emotion to log
     */
    private void logEmotion(Emotion emotion) {
        // Add the log
        logManager.addLog(emotion);

        // Update UI
        updateLogCount();

        // Show confirmation toast
        Toast.makeText(this,
                emotion.getEmoji() + " " + emotion.getDisplayName() + " logged!",
                Toast.LENGTH_SHORT).show();

        // Optional: Add visual feedback with button animation
        animateButton(emotion);
    }

    /**
     * Animate button press for better user feedback
     *
     * @param emotion The emotion whose button was pressed
     */
    private void animateButton(Emotion emotion) {
        // Find the button for this emotion
        for (Button button : emotionButtons) {
            if (button.getText().toString().contains(emotion.getDisplayName())) {
                button.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                button.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .start();
                            }
                        })
                        .start();
                break;
            }
        }
    }

    /**
     * Update the log count display
     */
    private void updateLogCount() {
        int count = logManager.getLogCount();
        tvLogCount.setText("Total Logs: " + count);
    }

    /**
     * Navigate to the LogsActivity to view all logs
     */
    private void navigateToLogs() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to the SummaryActivity to view statistics
     */
    private void navigateToSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }

    /**
     * Refresh data when activity resumes
     * This ensures the log count is always up-to-date
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateLogCount();
    }
}