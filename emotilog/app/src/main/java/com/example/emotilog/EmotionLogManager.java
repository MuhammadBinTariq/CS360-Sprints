//package com.example.emotilog;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
//import com.google.gson.Gson;
//
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
///**
// * EmotionLogManager is a singleton class that manages all emotion logs.
// *
// * Design Rationale:
// * - Singleton pattern ensures single source of truth for all emotion logs
// * - Provides centralized data management and business logic
// * - Uses SharedPreferences with Gson for simple persistence
// * - Separates data management from UI concerns (separation of concerns)
// * - Thread-safe singleton implementation
// *
// * Outstanding Issues:
// * - For production, consider using Room database for better performance with large datasets
// * - Current implementation loads all data into memory
// *
// * @author Your Name
// * @version 1.0
// */
//public class EmotionLogManager {
//    private static EmotionLogManager instance;
//    private List<EmotionLog> emotionLogs;
//    private SharedPreferences sharedPreferences;
//    private Gson gson;
//    private static final String PREFS_NAME = "EmotiLogPrefs";
//    private static final String LOGS_KEY = "emotion_logs";
//
//    /**
//     * Private constructor to prevent direct instantiation
//     *
//     * @param context Application context
//     */
//    private EmotionLogManager(Context context) {
//        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        gson = new Gson();
//        emotionLogs = loadLogs();
//    }
//
//    /**
//     * Get the singleton instance of EmotionLogManager
//     * Thread-safe implementation
//     *
//     * @param context Application context
//     * @return The singleton instance
//     */
//    public static synchronized EmotionLogManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new EmotionLogManager(context.getApplicationContext());
//        }
//        return instance;
//    }
//
//    /**
//     * Add a new emotion log
//     *
//     * @param emotionLog The emotion log to add
//     */
//    public void addLog(EmotionLog emotionLog) {
//        emotionLogs.add(emotionLog);
//        Collections.sort(emotionLogs); // Keep sorted by timestamp (most recent first)
//        saveLogs();
//    }
//
//    /**
//     * Add a new emotion log with just the emotion type
//     * Creates a new EmotionLog with current timestamp
//     *
//     * @param emotion The emotion to log
//     */
//    public void addLog(Emotion emotion) {
//        EmotionLog log = new EmotionLog(emotion);
//        addLog(log);
//    }
//
//    /**
//     * Get all emotion logs
//     *
//     * @return List of all emotion logs
//     */
//    public List<EmotionLog> getAllLogs() {
//        return new ArrayList<>(emotionLogs);
//    }
//
//    /**
//     * Get logs for a specific date
//     *
//     * @param date The date to filter by
//     * @return List of logs for the specified date
//     */
//    public List<EmotionLog> getLogsForDate(Date date) {
//        List<EmotionLog> dateLogs = new ArrayList<>();
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//
//        cal1.setTime(date);
//
//        for (EmotionLog log : emotionLogs) {
//            cal2.setTime(log.getTimestamp());
//
//            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
//                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
//                dateLogs.add(log);
//            }
//        }
//
//        return dateLogs;
//    }
//
//    /**
//     * Get summary of emotion counts for a specific date
//     *
//     * @param date The date to get summary for
//     * @return Map of emotion types to their counts
//     */
//    public Map<Emotion, Integer> getSummaryForDate(Date date) {
//        Map<Emotion, Integer> summary = new HashMap<>();
//        List<EmotionLog> dateLogs = getLogsForDate(date);
//
//        // Initialize all emotions with count 0
//        for (Emotion emotion : Emotion.values()) {
//            summary.put(emotion, 0);
//        }
//
//        // Count occurrences
//        for (EmotionLog log : dateLogs) {
//            Emotion emotion = log.getEmotion();
//            summary.put(emotion, summary.get(emotion) + 1);
//        }
//
//        return summary;
//    }
//
//    /**
//     * Get total count of logs for a specific date
//     *
//     * @param date The date to count logs for
//     * @return Total number of logs
//     */
//    public int getTotalLogsForDate(Date date) {
//        return getLogsForDate(date).size();
//    }
//
//    /**
//     * Get all unique dates that have logs
//     *
//     * @return List of dates with logs
//     */
//    public List<String> getUniqueDates() {
//        Map<String, Boolean> dateMap = new HashMap<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
//
//        for (EmotionLog log : emotionLogs) {
//            String dateStr = sdf.format(log.getTimestamp());
//            dateMap.put(dateStr, true);
//        }
//
//        return new ArrayList<>(dateMap.keySet());
//    }
//
//    /**
//     * Delete a specific log
//     *
//     * @param log The log to delete
//     * @return true if deleted successfully
//     */
//    public boolean deleteLog(EmotionLog log) {
//        boolean removed = emotionLogs.remove(log);
//        if (removed) {
//            saveLogs();
//        }
//        return removed;
//    }
//
//    /**
//     * Clear all logs
//     */
//    public void clearAllLogs() {
//        emotionLogs.clear();
//        saveLogs();
//    }
//
//    /**
//     * Save logs to SharedPreferences
//     */
//    private void saveLogs() {
//        String json = gson.toJson(emotionLogs);
//        sharedPreferences.edit().putString(LOGS_KEY, json).apply();
//    }
//
//    /**
//     * Load logs from SharedPreferences
//     *
//     * @return List of loaded logs, or empty list if none exist
//     */
//    private List<EmotionLog> loadLogs() {
//        String json = sharedPreferences.getString(LOGS_KEY, null);
//        if (json == null) {
//            return new ArrayList<>();
//        }
//
//        Type type = new TypeToken<ArrayList<EmotionLog>>(){}.getType();
//        List<EmotionLog> logs = gson.fromJson(json, type);
//
//        if (logs == null) {
//            return new ArrayList<>();
//        }
//
//        Collections.sort(logs);
//        return logs;
//    }
//
//    /**
//     * Get the count of logs
//     *
//     * @return Total number of logs
//     */
//    public int getLogCount() {
//        return emotionLogs.size();
//    }
//}

package com.example.emotilog;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * EmotionLogManager is a singleton class that manages all emotion logs.
 *
 * Design Rationale:
 * - Singleton pattern ensures single source of truth for all emotion logs
 * - Provides centralized data management and business logic
 * - Session-only memory - data resets when app is closed
 * - Separates data management from UI concerns (separation of concerns)
 * - Thread-safe singleton implementation
 *
 * Outstanding Issues: None
 *
 * @author Your Name
 * @version 1.0
 */
public class EmotionLogManager {
    private static EmotionLogManager instance;
    private List<EmotionLog> emotionLogs;

    /**
     * Private constructor to prevent direct instantiation
     * Initializes empty list for session-only storage
     *
     * @param context Application context (not used but kept for consistency)
     */
    private EmotionLogManager(Context context) {
        emotionLogs = new ArrayList<>();
    }

    /**
     * Get the singleton instance of EmotionLogManager
     * Thread-safe implementation
     *
     * @param context Application context
     * @return The singleton instance
     */
    public static synchronized EmotionLogManager getInstance(Context context) {
        if (instance == null) {
            instance = new EmotionLogManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Add a new emotion log
     * Session-only - data will be lost when app closes
     *
     * @param emotionLog The emotion log to add
     */
    public void addLog(EmotionLog emotionLog) {
        emotionLogs.add(emotionLog);
        Collections.sort(emotionLogs); // Keep sorted by timestamp (most recent first)
    }

    /**
     * Add a new emotion log with just the emotion type
     * Creates a new EmotionLog with current timestamp
     *
     * @param emotion The emotion to log
     */
    public void addLog(Emotion emotion) {
        EmotionLog log = new EmotionLog(emotion);
        addLog(log);
    }

    /**
     * Get all emotion logs
     *
     * @return List of all emotion logs
     */
    public List<EmotionLog> getAllLogs() {
        return new ArrayList<>(emotionLogs);
    }

    /**
     * Get logs for a specific date
     *
     * @param date The date to filter by
     * @return List of logs for the specified date
     */
    public List<EmotionLog> getLogsForDate(Date date) {
        List<EmotionLog> dateLogs = new ArrayList<>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date);

        for (EmotionLog log : emotionLogs) {
            cal2.setTime(log.getTimestamp());

            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                dateLogs.add(log);
            }
        }

        return dateLogs;
    }

    /**
     * Get summary of emotion counts for a specific date
     *
     * @param date The date to get summary for
     * @return Map of emotion types to their counts
     */
    public Map<Emotion, Integer> getSummaryForDate(Date date) {
        Map<Emotion, Integer> summary = new HashMap<>();
        List<EmotionLog> dateLogs = getLogsForDate(date);

        // Initialize all emotions with count 0
        for (Emotion emotion : Emotion.values()) {
            summary.put(emotion, 0);
        }

        // Count occurrences
        for (EmotionLog log : dateLogs) {
            Emotion emotion = log.getEmotion();
            summary.put(emotion, summary.get(emotion) + 1);
        }

        return summary;
    }

    /**
     * Get total count of logs for a specific date
     *
     * @param date The date to count logs for
     * @return Total number of logs
     */
    public int getTotalLogsForDate(Date date) {
        return getLogsForDate(date).size();
    }

    /**
     * Get all unique dates that have logs
     *
     * @return List of dates with logs
     */
    public List<String> getUniqueDates() {
        Map<String, Boolean> dateMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        for (EmotionLog log : emotionLogs) {
            String dateStr = sdf.format(log.getTimestamp());
            dateMap.put(dateStr, true);
        }

        return new ArrayList<>(dateMap.keySet());
    }

    /**
     * Delete a specific log
     *
     * @param log The log to delete
     * @return true if deleted successfully
     */
    public boolean deleteLog(EmotionLog log) {
        return emotionLogs.remove(log);
    }

    /**
     * Clear all logs
     * Useful for resetting the session
     */
    public void clearAllLogs() {
        emotionLogs.clear();
    }

    /**
     * Get the count of logs
     *
     * @return Total number of logs
     */
    public int getLogCount() {
        return emotionLogs.size();
    }
}