package com.example.emotilog;

import java.io.Serializable;

/**
 * An enumeration representing different emotions.
 * Design Rationale:
 * - Uses enum pattern for type safety and predefined emotion types
 * - Implements Serializable to allow passing between activities via Intent
 * - Each emotion has a display name and an emoji representation
 * - Provides a color resource for visual consistency in the UI
 *
 * @author Muhammad Bin Tariq
 * @version 0.4
 */
public enum Emotion implements Serializable {
    // Each emotion: display name, emoji, button color, background tint
    // Background tints are desaturated/pastel versions of the button color —
    // vibrant for upbeat moods, muted/cool for heavy ones.
    HAPPY    ("Happy",     "😊", "#FFD700", "#FFF8DC"),   // warm cream
    SAD      ("Sad",       "😢", "#4682B4", "#C8D8E8"),   // muted steel blue
    GRATEFUL ("Grateful",  "🙏", "#FF69B4", "#FFE4F0"),   // soft pink blush
    ANGRY    ("Angry",     "😠", "#FF4500", "#FFD0C0"),   // pale ember
    EXCITED  ("Excited",   "🎉", "#FF8C00", "#FFF0D0"),   // warm peach
    ANXIOUS  ("Anxious",   "😰", "#9370DB", "#E8E0F5"),   // pale lavender
    CALM     ("Calm",      "😌", "#20B2AA", "#D0F0EE"),   // light mint
    TIRED    ("Tired",     "😴", "#778899", "#D8DCE0");   // cool gray
    private final String displayName;
    private final String emoji;
    private final String colorCode;
    private final String backgroundTint;

    /**
     * Constructor for Emotion enum
     *
     * @param displayName The human-readable name of the emotion
     * @param emoji The emoji representation
     * @param colorCode The color code associated with this emotion
     */
    Emotion(String displayName, String emoji, String colorCode, String backgroundTint) {
        this.displayName    = displayName;
        this.emoji          = emoji;
        this.colorCode      = colorCode;
        this.backgroundTint = backgroundTint;
    }

    public String getDisplayName()    { return displayName; }
    public String getEmoji()          { return emoji; }
    public String getColorCode()      { return colorCode; }

    /**
     * Pastel/desaturated background color shown on the activity
     * when this emotion is logged.
     */
    public String getBackgroundTint() { return backgroundTint; }

    public String getFormattedDisplay() { return emoji + " " + displayName; }
}