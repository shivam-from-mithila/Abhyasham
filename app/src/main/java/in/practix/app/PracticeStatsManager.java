package in.practix.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PracticeStatsManager {

    private static final String PREF_NAME =
            "practice_stats";

    private static final String KEY_PRACTICED_IDS =
            "practiced_question_ids";

    private static final String KEY_CORRECT_COUNT =
            "correct_count";

    private static final String KEY_ATTEMPTED_COUNT =
            "attempted_count";

    private static final String KEY_STREAK =
            "day_streak";

    private static final String KEY_LAST_PRACTICE_DATE =
            "last_practice_date";

    // Prefix for topic-wise practiced questions
    private static final String KEY_TOPIC_PREFIX =
            "topic_practiced_";


    // =========================================
    // Shared Preferences
    // =========================================

    private static SharedPreferences getPreferences(
            Context context) {

        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }


    // =========================================
    // Save Quiz Result
    // =========================================

    public static void saveQuizResult(
            Context context,
            QuizResult result) {

        if (result == null) {
            return;
        }


        SharedPreferences preferences =
                getPreferences(context);


        SharedPreferences.Editor editor =
                preferences.edit();


        // =========================================
        // Existing totals
        // =========================================

        int oldCorrect =
                preferences.getInt(
                        KEY_CORRECT_COUNT,
                        0
                );


        int oldAttempted =
                preferences.getInt(
                        KEY_ATTEMPTED_COUNT,
                        0
                );


        // =========================================
        // Current quiz totals
        // =========================================

        int newCorrect =
                result.getCorrectCount();


        int newAttempted =
                result.getAttemptedCount();


        // =========================================
        // Save overall totals
        // =========================================

        editor.putInt(
                KEY_CORRECT_COUNT,
                oldCorrect + newCorrect
        );


        editor.putInt(
                KEY_ATTEMPTED_COUNT,
                oldAttempted + newAttempted
        );


        // =========================================
        // Overall unique practiced questions
        // =========================================

        Set<String> practicedIds =
                new HashSet<>(
                        preferences.getStringSet(
                                KEY_PRACTICED_IDS,
                                new HashSet<>()
                        )
                );


        // =========================================
        // IMPORTANT:
        // Accumulate topic-wise IDs locally first
        // =========================================

        Map<String, Set<String>>
                topicPracticedIds =
                new HashMap<>();


        for (Question question :
                result.getQuestions()) {

            if (question == null) {
                continue;
            }


            String questionId =
                    question.getId();


            String topicId =
                    question.getTopicId();


            String selectedAnswer =
                    question.getSelectedAnswer();


            // =========================================
            // Only answered questions count
            // =========================================

            if (questionId == null ||
                    questionId.isEmpty() ||
                    selectedAnswer == null ||
                    selectedAnswer.isEmpty()) {

                continue;
            }


            // =========================================
            // Overall practiced question
            // =========================================

            practicedIds.add(
                    questionId
            );


            // =========================================
            // Topic-wise practiced question
            // =========================================

            if (topicId == null ||
                    topicId.isEmpty()) {

                continue;
            }


            // -----------------------------------------
            // Get local set for this topic
            // -----------------------------------------

            Set<String> ids =
                    topicPracticedIds.get(
                            topicId
                    );


            if (ids == null) {

                ids =
                        new HashSet<>(
                                preferences.getStringSet(
                                        KEY_TOPIC_PREFIX + topicId,
                                        new HashSet<>()
                                )
                        );


                topicPracticedIds.put(
                        topicId,
                        ids
                );
            }


            // -----------------------------------------
            // Add current question
            // -----------------------------------------

            ids.add(
                    questionId
            );
        }


        // =========================================
        // Save topic-wise sets
        // =========================================

        for (Map.Entry<String, Set<String>> entry :
                topicPracticedIds.entrySet()) {

            String topicId =
                    entry.getKey();


            Set<String> ids =
                    entry.getValue();


            editor.putStringSet(
                    KEY_TOPIC_PREFIX + topicId,
                    ids
            );
        }


        // =========================================
        // Save overall practiced IDs
        // =========================================

        editor.putStringSet(
                KEY_PRACTICED_IDS,
                practicedIds
        );


        // =========================================
        // Update day streak
        // =========================================

        updateDayStreak(
                preferences,
                editor
        );


        // =========================================
        // Save everything
        // =========================================

        editor.apply();
    }


    // =========================================
    // Day Streak
    // =========================================

    private static void updateDayStreak(
            SharedPreferences preferences,
            SharedPreferences.Editor editor) {

        String today =
                getTodayDate();


        String lastDate =
                preferences.getString(
                        KEY_LAST_PRACTICE_DATE,
                        null
                );


        int oldStreak =
                preferences.getInt(
                        KEY_STREAK,
                        0
                );


        // =========================================
        // First practice
        // =========================================

        if (lastDate == null) {

            editor.putInt(
                    KEY_STREAK,
                    1
            );


            editor.putString(
                    KEY_LAST_PRACTICE_DATE,
                    today
            );


            return;
        }


        // =========================================
        // Same day
        // =========================================

        if (today.equals(lastDate)) {

            return;
        }


        // =========================================
        // Consecutive day
        // =========================================

        if (isYesterday(lastDate)) {

            editor.putInt(
                    KEY_STREAK,
                    oldStreak + 1
            );

        } else {

            // Missed one or more days

            editor.putInt(
                    KEY_STREAK,
                    1
            );
        }


        editor.putString(
                KEY_LAST_PRACTICE_DATE,
                today
        );
    }


    // =========================================
    // Get Today Date
    // =========================================

    private static String getTodayDate() {

        return new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(
                Calendar.getInstance().getTime()
        );
    }


    // =========================================
    // Check Yesterday
    // =========================================

    private static boolean isYesterday(
            String date) {

        try {

            SimpleDateFormat format =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );


            Calendar yesterday =
                    Calendar.getInstance();


            yesterday.add(
                    Calendar.DAY_OF_YEAR,
                    -1
            );


            String yesterdayDate =
                    format.format(
                            yesterday.getTime()
                    );


            return yesterdayDate.equals(
                    date
            );

        } catch (Exception e) {

            return false;
        }
    }


    // =========================================
    // Overall Practiced Questions
    // =========================================

    public static int getPracticedQuestions(
            Context context) {

        SharedPreferences preferences =
                getPreferences(context);


        Set<String> practicedIds =
                preferences.getStringSet(
                        KEY_PRACTICED_IDS,
                        new HashSet<>()
                );


        if (practicedIds == null) {

            return 0;
        }


        return practicedIds.size();
    }


    // =========================================
    // Get Practiced Question IDs For Topic
    // =========================================

    public static Set<String>
    getPracticedQuestionIdsForTopic(
            Context context,
            String topicId) {

        Set<String> emptySet =
                new HashSet<>();


        if (topicId == null ||
                topicId.isEmpty()) {

            return emptySet;
        }


        SharedPreferences preferences =
                getPreferences(context);


        String topicKey =
                KEY_TOPIC_PREFIX + topicId;


        Set<String> topicQuestionIds =
                preferences.getStringSet(
                        topicKey,
                        emptySet
                );


        if (topicQuestionIds == null) {

            return emptySet;
        }


        // Return a copy
        return new HashSet<>(
                topicQuestionIds
        );
    }


    // =========================================
    // Topic-wise Practiced Questions
    // =========================================

    public static int getPracticedQuestionsForTopic(
            Context context,
            String topicId) {

        if (topicId == null ||
                topicId.isEmpty()) {

            return 0;
        }


        SharedPreferences preferences =
                getPreferences(context);


        String topicKey =
                KEY_TOPIC_PREFIX + topicId;


        Set<String> topicQuestionIds =
                preferences.getStringSet(
                        topicKey,
                        new HashSet<>()
                );


        if (topicQuestionIds == null) {

            return 0;
        }


        return topicQuestionIds.size();
    }


    // =========================================
    // Correct Answers
    // =========================================

    public static int getCorrectAnswers(
            Context context) {

        return getPreferences(context)
                .getInt(
                        KEY_CORRECT_COUNT,
                        0
                );
    }


    // =========================================
    // Attempted Questions
    // =========================================

    public static int getAttemptedQuestions(
            Context context) {

        return getPreferences(context)
                .getInt(
                        KEY_ATTEMPTED_COUNT,
                        0
                );
    }


    // =========================================
    // Accuracy
    // =========================================

    public static int getAccuracy(
            Context context) {

        int attempted =
                getAttemptedQuestions(
                        context
                );


        if (attempted == 0) {

            return 0;
        }


        int correct =
                getCorrectAnswers(
                        context
                );


        return (correct * 100)
                / attempted;
    }


    // =========================================
    // Day Streak
    // =========================================

    public static int getDayStreak(
            Context context) {

        return getPreferences(context)
                .getInt(
                        KEY_STREAK,
                        0
                );
    }


    // =========================================
    // Clear All Statistics
    // =========================================

    public static void clearAll(
            Context context) {

        getPreferences(context)
                .edit()
                .clear()
                .apply();
    }
}