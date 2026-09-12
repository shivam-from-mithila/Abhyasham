package in.practix.app;

public class Subject {

    private String id;
    private String name;
    private int questions;
    private String iconUrl;


    public Subject(
            String id,
            String name,
            int questions,
            String iconUrl) {

        this.id = id;
        this.name = name;
        this.questions = questions;
        this.iconUrl = iconUrl;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public int getQuestions() {
        return questions;
    }


    public String getIconUrl() {
        return iconUrl;
    }
}