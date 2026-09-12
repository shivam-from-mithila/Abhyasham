package in.practix.app;

public class QuizResultManager {

    private static QuizResult quizResult;


    // Save current quiz result
    public static void setResult(QuizResult result) {

        quizResult = result;
    }


    // Get current quiz result
    public static QuizResult getResult() {

        return quizResult;
    }


    // Clear result
    public static void clearResult() {

        quizResult = null;
    }
}