package com.example.emotilog;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * MainActivity — main screen displaying emotion buttons for quick logging.
 *
 *  Design Rationale:
 *  - Large rounded buttons give each emotion a distinct visual identity.
 *  - Pressing a button triggers two simultaneous effects:
 *      1. Scale-down/up animation on the button ("physical" press feel).
 *      2. Background color of the whole screen cross-fades to a tint
 *         that matches the emotion's mood (warm/vibrant or cool/muted).
 *  - ValueAnimator drives the background cross-fade so the transition is
 *    smooth rather than an instant snap.
 *  - Button shape is a plain rounded GradientDrawable — no StateListDrawable
 *    color swap, so the only visual change on press is the scale animation.
 *
 * @author Muhammad Bin Tariq
 * @version 0.5
 */
public class MainActivity extends AppCompatActivity {

    // Duration constants (ms)
    private static final int PRESS_DOWN_MS  = 80;    // button shrink
    private static final int PRESS_UP_MS    = 120;   // button return
    private static final int BG_FADE_MS     = 400;   // background cross-fade

    // Neutral starting background
    private static final int COLOR_DEFAULT_BG = Color.parseColor("#F5F5F5");

    private EmotionLogManager logManager;
    private View               rootLayout;   // target for background animation
    private TextView           tvLogCount;
    private Button[]           emotionButtons;

    // Tracks current background color so each animation starts from it
    private int currentBgColor = COLOR_DEFAULT_BG;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logManager = EmotionLogManager.getInstance(this);

        initializeViews();
        setupEmotionButtons();
        setupNavigationButtons();
        updateLogCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLogCount();
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    private void initializeViews() {
        rootLayout = findViewById(R.id.root_layout);
        rootLayout.setBackgroundColor(currentBgColor);

        tvLogCount = findViewById(R.id.tv_log_count);

        emotionButtons = new Button[]{
                findViewById(R.id.btn_happy),
                findViewById(R.id.btn_sad),
                findViewById(R.id.btn_grateful),
                findViewById(R.id.btn_angry),
                findViewById(R.id.btn_excited),
                findViewById(R.id.btn_anxious),
                findViewById(R.id.btn_calm),
                findViewById(R.id.btn_tired),
        };
    }

    /**
     * Assigns text, style, and click listener to each emotion button.
     * Background is a plain rounded GradientDrawable — no state color change,
     * because the scale animation already provides clear press feedback.
     */
    private void setupEmotionButtons() {
        Emotion[] emotions = Emotion.values();

        for (int i = 0; i < emotionButtons.length && i < emotions.length; i++) {
            final Emotion emotion = emotions[i];
            final Button  button  = emotionButtons[i];

            button.setText(emotion.getFormattedDisplay());
            button.setTextColor(Color.WHITE);
            button.setBackground(buildRoundedDrawable(emotion));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateButtonPress(button);
                    animateBackground(emotion);
                    logEmotion(emotion);
                }
            });
        }
    }

    private void setupNavigationButtons() {
        FloatingActionButton fabLogs    = findViewById(R.id.fab_view_logs);
        FloatingActionButton fabSummary = findViewById(R.id.fab_view_summary);

        fabLogs.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { navigateToLogs(); }
        });
        fabSummary.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { navigateToSummary(); }
        });
    }

    // -------------------------------------------------------------------------
    // Button press animation
    // -------------------------------------------------------------------------

    /**
     * Scales the button down then back up — mimics a physical button press.
     * Why scale instead of color:
     * Darkening the button would clash visually with the background fade
     * happening at the same time. A scale change is tactile and neutral.
     *
     * @param button The button that was tapped
     */
    private void animateButtonPress(final Button button) {
        button.animate()
                .scaleX(0.88f)
                .scaleY(0.88f)
                .setDuration(PRESS_DOWN_MS)
                .withEndAction(new Runnable() {
                    @Override public void run() {
                        button.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(PRESS_UP_MS)
                                .start();
                    }
                })
                .start();
    }

    // -------------------------------------------------------------------------
    // Background animation
    // -------------------------------------------------------------------------

    /**
     * Cross-fades the activity background from its current color to the
     * emotion's background tint.
     * How it works:
     *   - ValueAnimator ticks from 0→1 over BG_FADE_MS milliseconds.
     *   - ArgbEvaluator blends the two ARGB colors each frame, giving a
     *     smooth per-channel interpolation (not just a linear RGB blend).
     *   - onAnimationEnd commits the final color so the next press starts
     *     from whatever color the screen is currently showing.
     *
     * @param emotion Source of the target background tint
     */
    private void animateBackground(final Emotion emotion) {
        final int targetColor = Color.parseColor(emotion.getBackgroundTint());

        if (targetColor == currentBgColor) return; // already there, skip

        ValueAnimator animator = ValueAnimator.ofObject(
                new ArgbEvaluator(), currentBgColor, targetColor);
        animator.setDuration(BG_FADE_MS);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                rootLayout.setBackgroundColor((int) anim.getAnimatedValue());
            }
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                currentBgColor = targetColor; // commit so next fade starts here
            }
        });

        animator.start();
    }

    // -------------------------------------------------------------------------
    // Drawable factory
    // -------------------------------------------------------------------------

    /**
     * Builds a plain rounded rectangle GradientDrawable for a button.
     * Corner radius of 60px produces a pill-like shape at typical densities.
     *
     * @param emotion Source of the fill color
     * @return Ready-to-use GradientDrawable
     */
    private GradientDrawable buildRoundedDrawable(Emotion emotion) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(60f);
        shape.setColor(Color.parseColor(emotion.getColorCode()));
        return shape;
    }

    // -------------------------------------------------------------------------
    // The actual app logic
    // -------------------------------------------------------------------------

    private void logEmotion(Emotion emotion) {
        logManager.addLog(emotion);
        updateLogCount();
        Toast.makeText(this,
                emotion.getEmoji() + " " + emotion.getDisplayName() + " logged!",
                Toast.LENGTH_SHORT).show();
    }

    private void updateLogCount() {
        tvLogCount.setText("Total Logs: " + logManager.getLogCount());
    }

    private void navigateToLogs() {
        startActivity(new Intent(this, LogsActivity.class));
    }

    private void navigateToSummary() {
        startActivity(new Intent(this, SummaryActivity.class));
    }
}