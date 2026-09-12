package in.practix.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;

public class ResultActivity extends AppCompatActivity {

    private QuizResult quizResult;

    private TextView tvScore;
    private TextView tvCorrectCount;
    private TextView tvIncorrectCount;
    private TextView tvUnansweredCount;
    private TextView tvTotalQuestions;

    private MaterialButton btnReviewAnswers;
    private MaterialButton btnFinish;

    // Prevent duplicate saving
    private boolean resultSaved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);


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
        // Get Quiz Result
        // ====================================

        quizResult =
                QuizResultManager.getResult();


        if (quizResult == null) {

            finish();

            return;
        }


        // ====================================
        // Save Practice Statistics
        // ====================================
        //
        // Save only once for this ResultActivity
        // instance.


        // ====================================
        // Initialize Views
        // ====================================

        tvScore =
                findViewById(
                        R.id.tvScore
                );


        tvCorrectCount =
                findViewById(
                        R.id.tvCorrectCount
                );


        tvIncorrectCount =
                findViewById(
                        R.id.tvIncorrectCount
                );


        tvUnansweredCount =
                findViewById(
                        R.id.tvUnansweredCount
                );


        tvTotalQuestions =
                findViewById(
                        R.id.tvTotalQuestions
                );


        btnReviewAnswers =
                findViewById(
                        R.id.btnReviewAnswers
                );


        btnFinish =
                findViewById(
                        R.id.btnFinish
                );



        // ====================================
        // Show Result
        // ====================================

        tvScore.setText(
                quizResult.getScorePercentage()
                        + "%"
        );


        tvCorrectCount.setText(
                String.valueOf(
                        quizResult.getCorrectCount()
                )
        );


        tvIncorrectCount.setText(
                String.valueOf(
                        quizResult.getIncorrectCount()
                )
        );


        tvUnansweredCount.setText(
                String.valueOf(
                        quizResult.getUnansweredCount()
                )
        );


        tvTotalQuestions.setText(
                "Total Questions: "
                        + quizResult.getTotalQuestions()
        );


        // ====================================
        // Review Answers
        // ====================================

        btnReviewAnswers.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ResultActivity.this,
                            ReviewActivity.class
                    );


            startActivity(intent);
        });


        // ====================================
        // Finish
        // ====================================

        btnFinish.setOnClickListener(v -> {

            finishToSecondActivity();
        });
    }


    // =====================================================
    // GO TO SECOND ACTIVITY
    // =====================================================

    private void finishToSecondActivity() {

        // ---------------------------------
        // Clear temporary quiz result
        // ---------------------------------

        QuizResultManager.clearResult();


        // ---------------------------------
        // Open SecondActivity
        // ---------------------------------

        Intent intent =
                new Intent(
                        ResultActivity.this,
                        SecondActivity.class
                );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );


        startActivity(intent);

        finish();
    }


    // =====================================================
    // BACK BUTTON
    // =====================================================

    @Override
    public void onBackPressed() {

        finishToSecondActivity();
    }
}