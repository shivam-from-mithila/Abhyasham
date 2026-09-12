package in.practix.app;

import java.util.ArrayList;
import java.util.List;

public class QuestionsRepository {


    // =========================================
    // Get Questions
    // =========================================

    public static List<Question> getQuestions(
            List<String> selectedTopicIds) {

        List<Question> allQuestions =
                getLocalQuestions();


        List<Question> selectedQuestions =
                new ArrayList<>();


        if (selectedTopicIds == null ||
                selectedTopicIds.isEmpty()) {

            return selectedQuestions;
        }


        for (Question question : allQuestions) {

            if (selectedTopicIds.contains(
                    question.getTopicId())) {

                selectedQuestions.add(question);
            }
        }


        return selectedQuestions;
    }


    // =========================================
    // Local Questions
    // =========================================
    //
    // Temporary data source.
    // Later this will be replaced by API.
    //

    private static List<Question> getLocalQuestions() {

        List<Question> questions =
                new ArrayList<>();


        // -----------------------------------------
        // Polity - Indian Constitution
        // -----------------------------------------

        questions.add(new Question(
                "q1",
                "polity_1",
                "Which part of the Constitution of India deals with Fundamental Rights?",
                "Part I",
                "Part II",
                "Part III",
                "Part IV",
                "C",
                "Fundamental Rights are contained in Part III of the Constitution."
        ));


        // -----------------------------------------
        // Polity - Fundamental Rights
        // -----------------------------------------

        questions.add(new Question(
                "q2",
                "polity_2",
                "Who is known as the constitutional head of the Union Executive?",
                "Prime Minister",
                "President",
                "Chief Justice",
                "Home Minister",
                "B",
                "The President is the constitutional head of the Union Executive."
        ));


        // -----------------------------------------
        // Polity - Parliament
        // -----------------------------------------

        questions.add(new Question(
                "q3",
                "polity_3",
                "The Parliament of India consists of which of the following?",
                "Lok Sabha only",
                "Rajya Sabha only",
                "President and two Houses",
                "Prime Minister and two Houses",
                "C",
                "The Parliament consists of the President, Lok Sabha and Rajya Sabha."
        ));


        return questions;
    }
}