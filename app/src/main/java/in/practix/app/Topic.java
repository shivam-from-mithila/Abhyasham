package in.practix.app;

public class Topic {

    private final String id;
    private final String subjectId;
    private final String name;
    private final int questionCount;

    // Local UI selection state
    private boolean selected;


    public Topic(
            String id,
            String subjectId,
            String name,
            int questionCount) {

        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.questionCount = questionCount;

        this.selected = false;
    }


    public String getId() {
        return id;
    }


    public String getSubjectId() {
        return subjectId;
    }


    public String getName() {
        return name;
    }


    public int getQuestionCount() {
        return questionCount;
    }


    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}