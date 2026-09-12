package in.practix.app;

public class Question {

    private final String id;
    private final String topicId;
    private final String question;

    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;

    private final String correctAnswer;
    private final String explanation;

    // User's selected answer
    private String selectedAnswer;


    public Question(
            String id,
            String topicId,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer,
            String explanation) {

        this.id = id;
        this.topicId = topicId;
        this.question = question;

        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;

        this.correctAnswer = correctAnswer;
        this.explanation = explanation;

        this.selectedAnswer = null;
    }


    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }


    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}