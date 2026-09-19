package in.practix.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;

    private View apiLoader;

    private TextView apiLoaderTitle;

    private SubjectAdapter subjectAdapter;

    private HeaderAdapter headerAdapter;

    private SecondActivityViewModel viewModel;


    // =====================================================
    // ON CREATE VIEW
    // =====================================================

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );
    }


    // =====================================================
    // ON VIEW CREATED
    // =====================================================

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(
                view,
                savedInstanceState
        );


        // ====================================
        // VIEWMODEL
        // ====================================

        viewModel =
                new ViewModelProvider(
                        requireActivity()
                ).get(
                        SecondActivityViewModel.class
                );


        // ====================================
        // STATUS BAR
        // ====================================

        updateStatusBar();


        // ====================================
        // API LOADER
        // ====================================

        apiLoader =
                view.findViewById(
                        R.id.apiLoader
                );


        apiLoaderTitle =
                view.findViewById(
                        R.id.apiLoaderTitle
                );


        // ====================================
        // RECYCLER VIEW
        // ====================================

        recyclerView =
                view.findViewById(
                        R.id.subjectRecyclerView
                );


        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );


        // ====================================
        // HEADER ADAPTER
        // ====================================

        headerAdapter =
                new HeaderAdapter();


        // ====================================
        // RESTORE EXISTING DATA
        // ====================================

        if (!viewModel.subjectList.isEmpty()) {

            subjectAdapter =
                    new SubjectAdapter(
                            viewModel.subjectList,
                            calculateSubjectProgress()
                    );

        } else {

            subjectAdapter =
                    new SubjectAdapter(
                            viewModel.subjectList,
                            new HashMap<>()
                    );
        }


        // ====================================
        // CONCAT ADAPTER
        // ====================================

        ConcatAdapter concatAdapter =
                new ConcatAdapter(
                        headerAdapter,
                        subjectAdapter
                );


        recyclerView.setAdapter(
                concatAdapter
        );


        // ====================================
        // API LOADING
        // ====================================

        if (!viewModel.apiStarted) {

            apiLoader.setVisibility(
                    View.VISIBLE
            );

            viewModel.apiStarted = true;

            viewModel.apiFinished = false;

            loadSubjects();

        } else if (viewModel.apiFinished) {

            apiLoader.setVisibility(
                    View.GONE
            );

        } else {

            apiLoader.setVisibility(
                    View.VISIBLE
            );
        }


        // ====================================
        // HIDE / SHOW BOTTOM NAV
        // ====================================

        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(
                            @NonNull RecyclerView recyclerView,
                            int dx,
                            int dy
                    ) {

                        super.onScrolled(
                                recyclerView,
                                dx,
                                dy
                        );


                        // BottomNavigationView is
                        // handled by SecondActivity
                        // because it is outside
                        // this Fragment.
                    }
                }
        );


        // ====================================
        // DARK MODE SWITCH
        // ====================================

        SwitchMaterial darkModeSwitch =
                view.findViewById(
                        R.id.darkModeSwitch
                );


        ImageView sunMoon =
                view.findViewById(
                        R.id.sun_moon
                );


        boolean isDarkMode =
                (getResources()
                        .getConfiguration()
                        .uiMode
                        & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES;


        // ====================================
        // INITIAL STATE
        // ====================================

        darkModeSwitch.setChecked(
                isDarkMode
        );


        if (isDarkMode) {

            sunMoon.setImageResource(
                    R.drawable.moon
            );

        } else {

            sunMoon.setImageResource(
                    R.drawable.sun_icon
            );
        }


        // ====================================
        // LISTEN FOR DARK MODE CHANGES
        // ====================================

        darkModeSwitch.setOnCheckedChangeListener(
                (buttonView, checked) -> {

                    if (checked) {

                        sunMoon.setImageResource(
                                R.drawable.moon
                        );


                        if (AppCompatDelegate
                                .getDefaultNightMode()
                                != AppCompatDelegate.MODE_NIGHT_YES) {

                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_YES
                                    );
                        }

                    } else {

                        sunMoon.setImageResource(
                                R.drawable.sun_icon
                        );


                        if (AppCompatDelegate
                                .getDefaultNightMode()
                                != AppCompatDelegate.MODE_NIGHT_NO) {

                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_NO
                                    );
                        }
                    }
                }
        );
    }


    // =====================================================
    // STATUS BAR
    // =====================================================

    private void updateStatusBar() {

        if (!isAdded()) {
            return;
        }


        requireActivity()
                .getWindow()
                .setStatusBarColor(
                        ContextCompat.getColor(
                                requireContext(),
                                R.color.status_bar
                        )
                );


        boolean isDarkMode =
                (getResources()
                        .getConfiguration()
                        .uiMode
                        & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES;


        if (isDarkMode) {

            requireActivity()
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(0);

        } else {

            requireActivity()
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    );
        }
    }


    // =====================================================
    // CALCULATE CURRENT SUBJECT PROGRESS
    // =====================================================

    private Map<String, Integer> calculateSubjectProgress() {

        Map<String, Integer>
                subjectProgressMap =
                new HashMap<>();


        for (Subject subject :
                viewModel.subjectList) {

            if (subject == null) {
                continue;
            }


            String subjectId =
                    subject.getId();


            if (subjectId == null ||
                    subjectId.isEmpty()) {

                continue;
            }


            Set<String> practicedQuestionIds =
                    new HashSet<>();


            for (Topic topic :
                    viewModel.topicList) {

                if (topic == null) {
                    continue;
                }


                String topicSubjectId =
                        topic.getSubjectId();


                if (subjectId.equals(
                        topicSubjectId
                )) {

                    String topicId =
                            topic.getId();


                    if (topicId == null ||
                            topicId.isEmpty()) {

                        continue;
                    }


                    Set<String> ids =
                            PracticeStatsManager
                                    .getPracticedQuestionIdsForTopic(
                                            requireContext(),
                                            topicId
                                    );


                    if (ids != null) {

                        practicedQuestionIds
                                .addAll(ids);
                    }
                }
            }


            int practiced =
                    practicedQuestionIds.size();


            int total =
                    viewModel
                            .subjectQuestionCountMap
                            .getOrDefault(
                                    subjectId,
                                    0
                            );


            int progress = 0;


            if (total > 0) {

                progress =
                        (practiced * 100)
                                / total;
            }


            if (progress < 0) {
                progress = 0;
            }


            if (progress > 100) {
                progress = 100;
            }


            subjectProgressMap.put(
                    subjectId,
                    progress
            );
        }


        return subjectProgressMap;
    }


    // =====================================================
    // LOAD SUBJECTS
    // =====================================================

    private void loadSubjects() {

        if (!isAdded()) {
            return;
        }


        apiLoader.setVisibility(
                View.VISIBLE
        );


        ApiClient
                .getApiService()
                .getSubjects()
                .enqueue(
                        new Callback<List<Subject>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Subject>> call,
                                    Response<List<Subject>> response
                            ) {

                                if (!isAdded()) {
                                    return;
                                }


                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    viewModel.subjectList.clear();


                                    viewModel.subjectList.addAll(
                                            response.body()
                                    );


                                    viewModel.subjectsLoaded =
                                            true;


                                    // =================================
                                    // SHOW SUBJECTS IMMEDIATELY
                                    // =================================

                                    subjectAdapter =
                                            new SubjectAdapter(
                                                    viewModel.subjectList,
                                                    new HashMap<>()
                                            );


                                    ConcatAdapter concatAdapter =
                                            new ConcatAdapter(
                                                    headerAdapter,
                                                    subjectAdapter
                                            );


                                    recyclerView.setAdapter(
                                            concatAdapter
                                    );


                                    // =================================
                                    // CONTINUE API CHAIN
                                    // =================================

                                    loadTopics();

                                } else {

                                    viewModel.apiFinished =
                                            true;


                                    apiLoader.setVisibility(
                                            View.GONE
                                    );
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Subject>> call,
                                    Throwable t
                            ) {

                                if (!isAdded()) {
                                    return;
                                }


                                viewModel.apiFinished =
                                        true;


                                apiLoader.setVisibility(
                                        View.GONE
                                );
                            }
                        }
                );
    }


    // =====================================================
    // LOAD TOPICS
    // =====================================================

    private void loadTopics() {

        if (!isAdded()) {
            return;
        }


        ApiClient
                .getApiService()
                .getTopics()
                .enqueue(
                        new Callback<List<Topic>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Topic>> call,
                                    Response<List<Topic>> response
                            ) {

                                if (!isAdded()) {
                                    return;
                                }


                                viewModel.topicList.clear();


                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    viewModel.topicList.addAll(
                                            response.body()
                                    );


                                    viewModel.topicsLoaded =
                                            true;


                                    loadQuestions();

                                } else {

                                    viewModel.apiFinished =
                                            true;


                                    updateSubjectProgress();


                                    apiLoader.setVisibility(
                                            View.GONE
                                    );
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Topic>> call,
                                    Throwable t
                            ) {

                                if (!isAdded()) {
                                    return;
                                }


                                viewModel.topicList.clear();


                                viewModel.apiFinished =
                                        true;


                                updateSubjectProgress();


                                apiLoader.setVisibility(
                                        View.GONE
                                );
                            }
                        }
                );
    }


    // =====================================================
    // UPDATE SUBJECT PROGRESS
    // =====================================================

    private void updateSubjectProgress() {

        if (!isAdded()
                || recyclerView == null
                || apiLoader == null) {

            return;
        }


        Map<String, Integer>
                subjectProgressMap =
                calculateSubjectProgress();


        subjectAdapter =
                new SubjectAdapter(
                        viewModel.subjectList,
                        subjectProgressMap
                );


        ConcatAdapter concatAdapter =
                new ConcatAdapter(
                        headerAdapter,
                        subjectAdapter
                );


        recyclerView.setAdapter(
                concatAdapter
        );


        // ====================================
        // HIDE LOADER ONLY AFTER API FINISH
        // ====================================

        if (viewModel.apiFinished) {

            apiLoader.setVisibility(
                    View.GONE
            );
        }
    }


    // =====================================================
    // LOAD QUESTIONS
    // =====================================================

    private void loadQuestions() {

        if (!isAdded()) {
            return;
        }


        ApiClient
                .getApiService()
                .getQuestions()
                .enqueue(
                        new Callback<List<Question>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Question>> call,
                                    Response<List<Question>> response
                            ) {

                                if (!isAdded()) {
                                    return;
                                }


                                viewModel
                                        .subjectQuestionCountMap
                                        .clear();


                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    List<Question> questions =
                                            response.body();


                                    // =================================
                                    // topicId -> subjectId
                                    // =================================

                                    Map<String, String>
                                            topicToSubjectMap =
                                            new HashMap<>();


                                    for (Topic topic :
                                            viewModel.topicList) {

                                        if (topic == null) {
                                            continue;
                                        }


                                        topicToSubjectMap.put(
                                                topic.getId(),
                                                topic.getSubjectId()
                                        );
                                    }


                                    // =================================
                                    // COUNT REAL QUESTIONS
                                    // =================================

                                    for (Question question :
                                            questions) {

                                        if (question == null) {
                                            continue;
                                        }


                                        String subjectId =
                                                topicToSubjectMap.get(
                                                        question.getTopicId()
                                                );


                                        if (subjectId == null) {
                                            continue;
                                        }


                                        int count =
                                                viewModel
                                                        .subjectQuestionCountMap
                                                        .getOrDefault(
                                                                subjectId,
                                                                0
                                                        );


                                        viewModel
                                                .subjectQuestionCountMap
                                                .put(
                                                        subjectId,
                                                        count + 1
                                                );
                                    }
                                }


                                // =================================
                                // API CHAIN FINISHED
                                // =================================

                                viewModel.questionsLoaded =
                                        true;


                                viewModel.apiFinished =
                                        true;


                                // =================================
                                // FINAL PROGRESS UPDATE
                                // =================================

                                updateSubjectProgress();
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Question>> call,
                                    Throwable t
                            ) {

                                if (!isAdded()) {
                                    return;
                                }


                                viewModel
                                        .subjectQuestionCountMap
                                        .clear();


                                // Even if questions API fails,
                                // the API chain is finished.

                                viewModel.apiFinished =
                                        true;


                                updateSubjectProgress();
                            }
                        }
                );
    }


    // =====================================================
    // REFRESH WHEN RETURNING TO HOME FRAGMENT
    // =====================================================

    @Override
    public void onResume() {

        super.onResume();


        if (headerAdapter != null) {

            headerAdapter.notifyDataSetChanged();
        }


        if (viewModel != null
                && !viewModel.subjectList.isEmpty()
                && !viewModel.topicList.isEmpty()) {

            updateSubjectProgress();
        }
    }
}