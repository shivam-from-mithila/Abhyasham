package in.practix.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicsActivity extends AppCompatActivity {

    private RecyclerView rvTopics;
    private TopicAdapter topicAdapter;

    private MaterialButton btnStartPractice;

    private TextView tvSubjectName;
    private TextView tvTotalQuestions;
    private View apiLoader;
    private TextView apiLoaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topics);

        // ====================================
// API Loader
// ====================================

        apiLoader =
                findViewById(R.id.apiLoader);

        apiLoaderTitle =
                findViewById(R.id.apiLoaderTitle);


        // ====================================
        // Receive Subject Data
        // ====================================

        String subjectName =
                getIntent().getStringExtra("SUBJECT_NAME");

        String subjectId =
                getIntent().getStringExtra("SUBJECT_ID");


        // ====================================
        // Header Subject Name
        // ====================================

        tvSubjectName =
                findViewById(R.id.tvSubjectName);

        if (subjectName != null) {
            tvSubjectName.setText(subjectName);
        }


        // ====================================
        // Total Questions
        // ====================================

        tvTotalQuestions =
                findViewById(R.id.tvTotalQuestions);


        // ====================================
        // Start Practice Button
        // ====================================

        btnStartPractice =
                findViewById(R.id.btnStartPractice);

        btnStartPractice.setEnabled(false);


        // ====================================
        // Status Bar
        // ====================================

        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        this,
                        R.color.mainBgColor
                )
        );


        // ====================================
        // Status Bar Icons
        // ====================================

        boolean isDarkMode =
                (getResources().getConfiguration().uiMode
                        & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES;


        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );


        controller.setAppearanceLightStatusBars(
                !isDarkMode
        );


        // ====================================
        // Back Button
        // ====================================

        FrameLayout btnBack =
                findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> {

            getOnBackPressedDispatcher()
                    .onBackPressed();

        });


        // ====================================
        // RecyclerView
        // ====================================

        rvTopics =
                findViewById(R.id.rvTopics);


        rvTopics.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // ====================================
        // Load Topics From API
        // ====================================

        loadTopics(subjectId);


        // ====================================
        // Start Practice
        // ====================================

        btnStartPractice.setOnClickListener(v -> {

            List<Topic> selectedTopics =
                    topicAdapter.getSelectedTopics();


            ArrayList<String> selectedTopicIds =
                    new ArrayList<>();


            for (Topic topic : selectedTopics) {

                selectedTopicIds.add(
                        topic.getId()
                );
            }


            Intent intent =
                    new Intent(
                            TopicsActivity.this,
                            QuestionsActivity.class
                    );


            intent.putStringArrayListExtra(
                    "SELECTED_TOPIC_IDS",
                    selectedTopicIds
            );


            startActivity(intent);
        });
    }


    // ========================================
    // Load Topics
    // ========================================

    private void loadTopics(String subjectId) {

        apiLoaderTitle.setText(
                "Getting Topics Ready..."
        );

        apiLoader.setVisibility(
                View.VISIBLE
        );


        ApiClient
                .getApiService()
                .getTopics()
                .enqueue(
                        new Callback<List<Topic>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Topic>> call,
                                    Response<List<Topic>> response) {

                                if (!response.isSuccessful()
                                        || response.body() == null) {

                                    apiLoader.setVisibility(
                                            View.GONE
                                    );

                                    return;
                                }


                                // ====================================
                                // Filter By Subject ID
                                // ====================================

                                List<Topic> filteredTopics =
                                        new ArrayList<>();


                                for (Topic topic :
                                        response.body()) {

                                    if (subjectId != null
                                            && subjectId.equals(
                                            topic.getSubjectId())) {

                                        filteredTopics.add(
                                                topic
                                        );
                                    }
                                }


                                // ====================================
                                // Topic Adapter
                                // ====================================

                                topicAdapter =
                                        new TopicAdapter(
                                                filteredTopics,
                                                totalQuestions -> {

                                                    if (totalQuestions == 0) {

                                                        tvTotalQuestions.setText(
                                                                "Total "
                                                                        + getTotalQuestionCount(
                                                                        filteredTopics
                                                                )
                                                                        + " Questions"
                                                        );

                                                    } else {

                                                        tvTotalQuestions.setText(
                                                                totalQuestions
                                                                        + " Questions Selected"
                                                        );
                                                    }


                                                    btnStartPractice.setEnabled(
                                                            totalQuestions > 0
                                                    );
                                                }
                                        );


                                rvTopics.setAdapter(
                                        topicAdapter
                                );


                                // ====================================
                                // Initial Total Questions
                                // ====================================

                                tvTotalQuestions.setText(
                                        "Total "
                                                + getTotalQuestionCount(
                                                filteredTopics
                                        )
                                                + " Questions"
                                );

                                apiLoader.setVisibility(
                                        View.GONE
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Topic>> call,
                                    Throwable t) {

                                // API failed.
                                // Error handling can be
                                // added later.
                                apiLoader.setVisibility(
                                        View.GONE
                                );
                            }
                        }
                );
    }


    // ========================================
    // Total Question Count
    // ========================================

    private int getTotalQuestionCount(
            List<Topic> topics) {

        int total = 0;


        for (Topic topic : topics) {

            total += topic.getQuestionCount();
        }


        return total;
    }
}