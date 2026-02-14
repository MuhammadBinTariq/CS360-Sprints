package com.example.emotilog;

import java.io.Serializable;

/**
 * Emotion class represents the different types of emotions that can be logged.
 *
 * Design Rationale:
 * - Uses enum pattern for type safety and predefined emotion types
 * - Implements Serializable to allow passing between activities via Intent
 * - Each emotion has a display name and an emoji representation
 * - Provides a color resource for visual consistency in the UI
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public enum Emotion implements Serializable {
    HAPPY("Happy", "😊", "#FFD700"),
    SAD("Sad", "😢", "#4682B4"),
    GRATEFUL("Grateful", "🙏", "#FF69B4"),
    ANGRY("Angry", "😠", "#FF4500"),
    EXCITED("Excited", "🎉", "#FF8C00"),
    ANXIOUS("Anxious", "😰", "#9370DB"),
    CALM("Calm", "😌", "#20B2AA"),
    TIRED("Tired", "😴", "#778899"),
    SURPRISED("Surprised", "😲", "#FFD700");

    private final String displayName;
    private final String emoji;
    private final String colorCode;

    /**
     * Constructor for Emotion enum
     *
     * @param displayName The human-readable name of the emotion
     * @param emoji The emoji representation
     * @param colorCode The color code associated with this emotion
     */
    Emotion(String displayName, String emoji, String colorCode) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.colorCode = colorCode;
    }

    /**
     * Get the display name of the emotion
     *
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the emoji representation
     *
     * @return The emoji string
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * Get the color code for this emotion
     *
     * @return The hex color code
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Get formatted display string combining emoji and name
     *
     * @return Formatted string for display
     */
    public String getFormattedDisplay() {
        return emoji + " " + displayName;
    }
}