package in.practix.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private View apiLoader;

    private TextView apiLoaderTitle;

    private BottomNavigationView bottomNavigationView;

    private SubjectAdapter subjectAdapter;

    private HeaderAdapter headerAdapter;

    // =====================================================
    // VIEWMODEL
    // =====================================================

    private SecondActivityViewModel viewModel;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);


        // ====================================
        // VIEWMODEL
        // ====================================

        viewModel =
                new ViewModelProvider(this)
                        .get(SecondActivityViewModel.class);


        // ====================================
        // INITIAL STATUS BAR
        // ====================================

        updateStatusBar();


        // ====================================
        // CHECK SPLASH REQUIREMENT
        // ====================================

        boolean skipSplash =
                getIntent().getBooleanExtra(
                        "SKIP_SPLASH",
                        false
                );


        boolean firstLaunch =
                savedInstanceState == null
                        && !skipSplash;


        // ====================================
        // FIRST APP LAUNCH
        // → SHOW BRANDING SPLASH
        // ====================================

        if (firstLaunch) {

            setContentView(
                    R.layout.layout_splash_branding
            );


            new Handler(
                    Looper.getMainLooper()
            ).postDelayed(
                    () -> {

                        if (!isFinishing()
                                && !isDestroyed()) {

                            initializeMainScreen();
                        }

                    },
                    4000
            );


        } else {

            // ====================================
            // RECREATION / BOTTOM NAV RETURN
            // → NO BRANDING SPLASH
            // ====================================

            initializeMainScreen();
        }
    }


    // =====================================================
    // STATUS BAR
    // =====================================================

    private void updateStatusBar() {

        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        this,
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

            // Dark mode → WHITE status bar icons

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(0);

        } else {

            // Light mode → DARK status bar icons

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    );
        }
    }


    // =====================================================
    // INITIALIZE MAIN SCREEN
    // =====================================================

    private void initializeMainScreen() {

        // ====================================
        // MAIN ACTIVITY LAYOUT
        // ====================================

        setContentView(
                R.layout.activity_second
        );


        // ====================================
        // API LOADER
        // ====================================

        apiLoader =
                findViewById(
                        R.id.apiLoader
                );


        apiLoaderTitle =
                findViewById(
                        R.id.apiLoaderTitle
                );


        // ====================================
        // STATUS BAR
        // ====================================

        updateStatusBar();


        // ====================================
        // RECYCLER VIEW
        // ====================================

        recyclerView =
                findViewById(
                        R.id.subjectRecyclerView
                );


        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
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

            // API chain already completed previously.
            // Do not show loader again after recreation.

            apiLoader.setVisibility(
                    View.GONE
            );

        } else {

            // API chain was started but is not finished yet.

            apiLoader.setVisibility(
                    View.VISIBLE
            );
        }


        // ====================================
        // BOTTOM NAVIGATION
        // ====================================

        bottomNavigationView =
                findViewById(
                        R.id.bottomNavigationView
                );


        bottomNavigationView.setOnItemSelectedListener(
                item -> {

                    int id =
                            item.getItemId();


                    // ====================================
                    // HOME
                    // ====================================

                    if (id == R.id.nav_home) {

                        return true;


                        // ====================================
                        // QUICK TEST
                        // ====================================

                    } else if (
                            id == R.id.nav_quick_test
                    ) {

                        Intent intent =
                                new Intent(
                                        SecondActivity.this,
                                        QuickTestActivity.class
                                );


                        startActivity(intent);


                        overridePendingTransition(
                                R.anim.slide_in_left,
                                R.anim.silde_out_right
                        );


                        return true;


                        // ====================================
                        // PROFILE
                        // ====================================

                    } else if (
                            id == R.id.nav_profile
                    ) {

                        Intent intent =
                                new Intent(
                                        SecondActivity.this,
                                        ProfileActivity.class
                                );


                        startActivity(intent);


                        overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        );


                        return true;
                    }


                    return false;
                }
        );


        // ====================================
        // HOME SELECTED BY DEFAULT
        // ====================================

        bottomNavigationView.setSelectedItemId(
                R.id.nav_home
        );


        // ====================================
        // HIDE / SHOW BOTTOM NAVIGATION
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


                        if (dy > 0) {

                            // Scrolling DOWN → hide

                            bottomNavigationView
                                    .animate()
                                    .translationY(
                                            bottomNavigationView
                                                    .getHeight()
                                    )
                                    .setDuration(200)
                                    .start();


                        } else if (dy < 0) {

                            // Scrolling UP → show

                            bottomNavigationView
                                    .animate()
                                    .translationY(0)
                                    .setDuration(200)
                                    .start();
                        }
                    }
                }
        );


        // ====================================
        // DARK MODE SWITCH
        // ====================================

        SwitchMaterial darkModeSwitch =
                findViewById(
                        R.id.darkModeSwitch
                );


        ImageView sunMoon =
                findViewById(
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
                                            this,
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
                    viewModel.subjectQuestionCountMap
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
                                    Response<List<Subject>> response) {

                                if (isFinishing()
                                        || isDestroyed()) {
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

                                    // Subjects failed.
                                    // Stop loader.

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
                                    Throwable t) {

                                if (isFinishing()
                                        || isDestroyed()) {
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

        ApiClient
                .getApiService()
                .getTopics()
                .enqueue(
                        new Callback<List<Topic>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Topic>> call,
                                    Response<List<Topic>> response) {

                                if (isFinishing()
                                        || isDestroyed()) {
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
                                    Throwable t) {

                                if (isFinishing()
                                        || isDestroyed()) {
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

        if (recyclerView == null
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

        ApiClient
                .getApiService()
                .getQuestions()
                .enqueue(
                        new Callback<List<Question>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Question>> call,
                                    Response<List<Question>> response) {

                                if (isFinishing()
                                        || isDestroyed()) {
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
                                    Throwable t) {

                                if (isFinishing()
                                        || isDestroyed()) {
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
    // REFRESH WHEN RETURNING TO SECOND ACTIVITY
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();


        if (headerAdapter != null) {

            headerAdapter.notifyDataSetChanged();
        }


        if (!viewModel.subjectList.isEmpty()
                && !viewModel.topicList.isEmpty()) {

            updateSubjectProgress();
        }
    }


    // =====================================================
    // HANDLE NEW INTENT
    // =====================================================

    @Override
    protected void onNewIntent(
            Intent intent) {

        super.onNewIntent(
                intent
        );


        setIntent(
                intent
        );


        if (bottomNavigationView != null) {

            bottomNavigationView.setSelectedItemId(
                    R.id.nav_home
            );
        }
    }
}