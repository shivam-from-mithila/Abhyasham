package in.practix.app;

import java.util.ArrayList;
import java.util.List;

public class TopicData {

    public static List<Topic> getTopics(String subjectId) {

        List<Topic> topics = new ArrayList<>();

        if (subjectId == null) {
            return topics;
        }


        switch (subjectId) {

            // =================================
            // Polity
            // =================================

            case "subject_1":

                topics.add(new Topic(
                        "polity_1",
                        "subject_1",
                        "Indian Constitution",
                        20
                ));

                topics.add(new Topic(
                        "polity_2",
                        "subject_1",
                        "Fundamental Rights",
                        15
                ));

                topics.add(new Topic(
                        "polity_3",
                        "subject_1",
                        "Parliament",
                        20
                ));

                topics.add(new Topic(
                        "polity_4",
                        "subject_1",
                        "President",
                        15
                ));

                topics.add(new Topic(
                        "polity_5",
                        "subject_1",
                        "Supreme Court",
                        20
                ));

                break;


            // =================================
            // Geography
            // =================================

            case "subject_2":

                topics.add(new Topic(
                        "geography_1",
                        "subject_2",
                        "Physical Geography",
                        20
                ));

                topics.add(new Topic(
                        "geography_2",
                        "subject_2",
                        "Indian Geography",
                        25
                ));

                topics.add(new Topic(
                        "geography_3",
                        "subject_2",
                        "World Geography",
                        20
                ));

                break;


            // =================================
            // History
            // =================================

            case "subject_3":

                topics.add(new Topic(
                        "history_1",
                        "subject_3",
                        "Ancient India",
                        20
                ));

                topics.add(new Topic(
                        "history_2",
                        "subject_3",
                        "Medieval India",
                        20
                ));

                topics.add(new Topic(
                        "history_3",
                        "subject_3",
                        "Modern India",
                        25
                ));

                topics.add(new Topic(
                        "history_4",
                        "subject_3",
                        "Indian National Movement",
                        20
                ));

                break;


            // =================================
            // Economy
            // =================================

            case "subject_4":

                topics.add(new Topic(
                        "economy_1",
                        "subject_4",
                        "Basic Economics",
                        20
                ));

                topics.add(new Topic(
                        "economy_2",
                        "subject_4",
                        "Indian Economy",
                        25
                ));

                topics.add(new Topic(
                        "economy_3",
                        "subject_4",
                        "Banking",
                        20
                ));

                break;
        }


        return topics;
    }
}