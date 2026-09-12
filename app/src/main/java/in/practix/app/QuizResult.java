package in.practix.app;

import java.util.ArrayList;
import java.util.List;

public class QuizResult {

    private final List<Question> questions;

    // Topics selected for this quiz
    private final ArrayList<String> selectedTopicIds;

    private int correctCount;
    private int incorrectCount;
    private int unansweredCount;


    // =========================================
    // Constructor
    // =========================================

    public QuizResult(
            List<Question> questions,
            ArrayList<String> selectedTopicIds) {

        this.questions = questions;

        if (selectedTopicIds != null) {

            this.selectedTopicIds =
                    new ArrayList<>(selectedTopicIds);

        } else {

            this.selectedTopicIds =
                    new ArrayList<>();
        }

        calculateResult();
    }


    // =========================================
    // Calculate Result
    // =========================================

    private void calculateResult() {

        correctCount = 0;
        incorrectCount = 0;
        unansweredCount = 0;


        for (Question question : questions) {

            String selectedAnswer =
                    question.getSelectedAnswer();


            // ---------------------------------
            // Unanswered
            // ---------------------------------

            if (selectedAnswer == null ||
                    selectedAnswer.isEmpty()) {

                unansweredCount++;
            }


            // ---------------------------------
            // Correct
            // ---------------------------------

            else if (selectedAnswer.equals(
                    question.getCorrectAnswer())) {

                correctCount++;
            }


            // ---------------------------------
            // Incorrect
            // ---------------------------------

            else {

                incorrectCount++;
            }
        }
    }


    // =========================================
    // Get Questions
    // =========================================

    public List<Question> getQuestions() {

        return questions;
    }


    // =========================================
    // Get Selected Topic IDs
    // =========================================

    public ArrayList<String> getSelectedTopicIds() {

        return new ArrayList<>(
                selectedTopicIds
        );
    }


    // =========================================
    // Correct Count
    // =========================================

    public int getCorrectCount() {

        return correctCount;
    }


    // =========================================
    // Incorrect Count
    // =========================================

    public int getIncorrectCount() {

        return incorrectCount;
    }


    // =========================================
    // Unanswered Count
    // =========================================

    public int getUnansweredCount() {

        return unansweredCount;
    }


    // =========================================
    // Total Questions
    // =========================================

    public int getTotalQuestions() {

        return questions.size();
    }


    // =========================================
    // Attempted Questions
    // =========================================

    public int getAttemptedCount() {

        return correctCount +
                incorrectCount;
    }


    // =========================================
    // Score Percentage
    // =========================================

    public int getScorePercentage() {

        if (questions.isEmpty()) {

            return 0;
        }


        return (correctCount * 100)
                / questions.size();
    }
}