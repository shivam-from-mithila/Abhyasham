package in.practix.app;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondActivityViewModel extends ViewModel {

    // =====================================================
    // PERSISTENT HOME DATA
    // =====================================================

    public final ArrayList<Subject> subjectList =
            new ArrayList<>();

    public final ArrayList<Topic> topicList =
            new ArrayList<>();

    public final Map<String, Integer> subjectQuestionCountMap =
            new HashMap<>();


    // =====================================================
    // API STATE
    // =====================================================

    public boolean apiStarted = false;

    public boolean subjectsLoaded = false;

    public boolean topicsLoaded = false;

    public boolean questionsLoaded = false;
    public boolean apiFinished = false;


    // =====================================================
    // CHECK WHETHER COMPLETE DATA IS AVAILABLE
    // =====================================================

    public boolean isDataReady() {

        return subjectsLoaded
                && topicsLoaded
                && questionsLoaded;
    }
}