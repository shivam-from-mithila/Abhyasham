package in.practix.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private QuizResult quizResult;
    private List<Question> questionList;

    private RecyclerView rvReviewQuestions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);

        // --------------------------------
        // Status Bar
        // --------------------------------

        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        this,
                        R.color.mainBgColor
                )
        );


        // --------------------------------
        // Status Bar Icons
        // --------------------------------

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
        // Get Quiz Result
        // ====================================

        quizResult =
                QuizResultManager.getResult();

        if (quizResult == null) {
            finish();
            return;
        }


        // ====================================
        // Get Questions
        // ====================================

        questionList =
                quizResult.getQuestions();


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

        rvReviewQuestions =
                findViewById(R.id.rvReviewQuestions);


        rvReviewQuestions.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // ====================================
        // Review Adapter
        // ====================================

        ReviewQuestionAdapter adapter =
                new ReviewQuestionAdapter(
                        questionList
                );


        rvReviewQuestions.setAdapter(adapter);
    }
}