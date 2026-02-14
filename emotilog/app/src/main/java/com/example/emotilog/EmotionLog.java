package com.example.emotilog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * EmotionLog class represents a single emotion logging event.
 *
 * Design Rationale:
 * - Encapsulates all data for a single emotion log entry
 * - Uses Date object for timestamp to allow for easy formatting and comparison
 * - Implements Serializable for data persistence and transfer
 * - Follows JavaBean conventions with getters and setters
 * - Immutable ID using timestamp ensures unique identification
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class EmotionLog implements Serializable, Comparable<EmotionLog> {
    private static final long serialVersionUID = 1L;

    private Emotion emotion;
    private Date timestamp;
    private long id; // Using timestamp as unique ID

    /**
     * Default constructor
     * Initializes with current timestamp
     */
    public EmotionLog() {
        this.timestamp = new Date();
        this.id = timestamp.getTime();
    }

    /**
     * Constructor with emotion parameter
     *
     * @param emotion The emotion being logged
     */
    public EmotionLog(Emotion emotion) {
        this();
        this.emotion = emotion;
    }

    /**
     * Constructor with emotion and custom timestamp
     * Useful for testing or importing historical data
     *
     * @param emotion The emotion being logged
     * @param timestamp The timestamp for this log
     */
    public EmotionLog(Emotion emotion, Date timestamp) {
        this.emotion = emotion;
        this.timestamp = timestamp;
        this.id = timestamp.getTime();
    }

    /**
     * Get the emotion of this log entry
     *
     * @return The emotion
     */
    public Emotion getEmotion() {
        return emotion;
    }

    /**
     * Set the emotion for this log entry
     *
     * @param emotion The emotion to set
     */
    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    /**
     * Get the timestamp of this log entry
     *
     * @return The timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp for this log entry
     *
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        this.id = timestamp.getTime();
    }

    /**
     * Get the unique ID of this log entry
     *
     * @return The unique ID
     */
    public long getId() {
        return id;
    }

    /**
     * Get formatted timestamp string for display
     *
     * @return Formatted timestamp string (e.g., "Feb 14, 2026 10:30:45 AM")
     */
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.getDefault());
        return sdf.format(timestamp);
    }

    /**
     * Get formatted time only for display
     *
     * @return Formatted time string (e.g., "10:30:45 AM")
     */
    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return sdf.format(timestamp);
    }

    /**
     * Get formatted date only for display
     *
     * @return Formatted date string (e.g., "Feb 14, 2026")
     */
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(timestamp);
    }

    /**
     * Compare this log with another based on timestamp
     * Used for sorting logs chronologically
     *
     * @param other The other EmotionLog to compare with
     * @return Negative if this is earlier, positive if later, 0 if equal
     */
    @Override
    public int compareTo(EmotionLog other) {
        // Most recent first (reverse chronological order)
        return other.timestamp.compareTo(this.timestamp);
    }

    /**
     * String representation of this log entry
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return emotion.getFormattedDisplay() + " - " + getFormattedTimestamp();
    }
}