package in.practix.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionsActivity extends AppCompatActivity {

    // =====================================================
    // OPTION CARDS
    // =====================================================

    private MaterialCardView cardOptionA;
    private MaterialCardView cardOptionB;
    private MaterialCardView cardOptionC;
    private MaterialCardView cardOptionD;


    // =====================================================
    // BUTTONS
    // =====================================================

    private MaterialButton btnPrevious;
    private MaterialButton btnNext;


    // =====================================================
    // QUESTION PROGRESS
    // =====================================================

    private LinearProgressIndicator questionProgress;


    // =====================================================
    // QUESTION LIST
    // =====================================================

    private List<Question> questionList =
            new ArrayList<>();


    // =====================================================
    // SELECTED TOPIC IDS
    // =====================================================

    private ArrayList<String> selectedTopicIds =
            new ArrayList<>();


    // =====================================================
    // CURRENT QUESTION
    // =====================================================

    private int currentQuestionIndex = 0;


    // =====================================================
    // BACK BUTTON
    // =====================================================

    private FrameLayout btnBack;
    private View apiLoader;
    private TextView apiLoaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_questions
        );
        // ====================================
        // Status Bar
        // ====================================

        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        this,
                        R.color.status_bar
                )
        );


        boolean isDarkMode =
                AppCompatDelegate.getDefaultNightMode()
                        == AppCompatDelegate.MODE_NIGHT_YES;


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

        apiLoader =
                findViewById(R.id.apiLoader);

        apiLoaderTitle =
                findViewById(R.id.apiLoaderTitle);

        apiLoader.setVisibility(
                View.VISIBLE
        );

        apiLoaderTitle.setText(
                "Getting Questions Ready..."
        );


        // =================================================
        // BACK BUTTON
        // =================================================

        btnBack =
                findViewById(
                        R.id.btnBack
                );


        btnBack.setOnClickListener(v -> {

            getOnBackPressedDispatcher()
                    .onBackPressed();
        });


        // =================================================
        // RECEIVE SELECTED TOPIC IDS
        // =================================================

        ArrayList<String> receivedTopicIds =
                getIntent().getStringArrayListExtra(
                        "SELECTED_TOPIC_IDS"
                );


        if (receivedTopicIds != null) {

            selectedTopicIds =
                    new ArrayList<>(
                            receivedTopicIds
                    );
        }


        // =================================================
        // INITIALIZE OPTION CARDS
        // =================================================

        cardOptionA =
                findViewById(
                        R.id.cardOptionA
                );


        cardOptionB =
                findViewById(
                        R.id.cardOptionB
                );


        cardOptionC =
                findViewById(
                        R.id.cardOptionC
                );


        cardOptionD =
                findViewById(
                        R.id.cardOptionD
                );


        // =================================================
        // INITIALIZE BUTTONS
        // =================================================

        btnPrevious =
                findViewById(
                        R.id.btnPrevious
                );


        btnNext =
                findViewById(
                        R.id.btnNext
                );


        // =================================================
        // INITIALIZE PROGRESS
        // =================================================

        questionProgress =
                findViewById(
                        R.id.questionProgress
                );


        // =================================================
        // OPTION A
        // =================================================

        cardOptionA.setOnClickListener(v -> {

            selectOption(
                    cardOptionA
            );
        });


        // =================================================
        // OPTION B
        // =================================================

        cardOptionB.setOnClickListener(v -> {

            selectOption(
                    cardOptionB
            );
        });


        // =================================================
        // OPTION C
        // =================================================

        cardOptionC.setOnClickListener(v -> {

            selectOption(
                    cardOptionC
            );
        });


        // =================================================
        // OPTION D
        // =================================================

        cardOptionD.setOnClickListener(v -> {

            selectOption(
                    cardOptionD
            );
        });


        // =================================================
        // PREVIOUS BUTTON
        // =================================================

        btnPrevious.setOnClickListener(v -> {

            if (questionList == null ||
                    questionList.isEmpty()) {

                return;
            }


            if (currentQuestionIndex > 0) {

                currentQuestionIndex--;

                showQuestion();
            }
        });


        // =================================================
        // NEXT BUTTON
        // =================================================

        btnNext.setOnClickListener(v -> {

            if (questionList == null ||
                    questionList.isEmpty()) {

                return;
            }


            if (currentQuestionIndex ==
                    questionList.size() - 1) {

                submitQuiz();

            } else {

                currentQuestionIndex++;

                showQuestion();
            }
        });


        // =================================================
        // LOAD QUESTIONS
        // =================================================

        loadQuestionsFromApi();
    }


    // =====================================================
    // LOAD QUESTIONS FROM API
    // =====================================================

    private void loadQuestionsFromApi() {


        // =================================================
        // CHECK TOPIC SELECTION
        // =================================================

        if (selectedTopicIds == null ||
                selectedTopicIds.isEmpty()) {

            showNoQuestions();

            return;
        }


        // =================================================
        // DISABLE BUTTONS WHILE LOADING
        // =================================================

        btnPrevious.setEnabled(false);

        btnNext.setEnabled(false);


        TextView tvQuestion =
                findViewById(
                        R.id.tvQuestion
                );





        // =================================================
        // API CALL
        // =================================================

        ApiClient
                .getApiService()
                .getQuestions()
                .enqueue(
                        new Callback<List<Question>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Question>> call,
                                    Response<List<Question>> response) {


                                // ---------------------------------
                                // API RESPONSE CHECK
                                // ---------------------------------

                                if (!response.isSuccessful()
                                        || response.body() == null) {

                                    showNoQuestions();

                                    return;
                                }


                                // ---------------------------------
                                // ALL QUESTIONS FROM API
                                // ---------------------------------

                                List<Question> allQuestions =
                                        response.body();


                                // ---------------------------------
                                // CLEAR OLD LIST
                                // ---------------------------------

                                questionList =
                                        new ArrayList<>();


                                // ---------------------------------
                                // FILTER BY TOPIC IDS
                                // ---------------------------------

                                for (Question question :
                                        allQuestions) {


                                    if (question == null) {

                                        continue;
                                    }


                                    String topicId =
                                            question.getTopicId();


                                    if (topicId != null
                                            && selectedTopicIds
                                            .contains(topicId)) {

                                        questionList.add(
                                                question
                                        );
                                    }
                                }


                                // ---------------------------------
                                // NO QUESTIONS
                                // ---------------------------------

                                if (questionList.isEmpty()) {

                                    showNoQuestions();

                                    return;
                                }


                                // ---------------------------------
                                // START FIRST QUESTION
                                // ---------------------------------

                                currentQuestionIndex = 0;

                                showQuestion();
                                apiLoader.setVisibility(
                                        View.GONE
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Question>> call,
                                    Throwable t) {

                                showNoQuestions();
                                apiLoader.setVisibility(
                                        View.GONE
                                );
                            }
                        }
                );
    }


    // =====================================================
    // NO QUESTIONS
    // =====================================================

    private void showNoQuestions() {

        questionList =
                new ArrayList<>();


        currentQuestionIndex = 0;


        TextView tvQuestion =
                findViewById(
                        R.id.tvQuestion
                );


        TextView tvQuestionNumber =
                findViewById(
                        R.id.tvQuestionNumber
                );


        tvQuestionNumber.setText(
                "Questions"
        );


        tvQuestion.setText(
                "No questions available for the selected topics."
        );


        btnPrevious.setEnabled(false);

        btnNext.setEnabled(false);


        questionProgress.setProgress(
                0
        );
    }


    // =====================================================
    // SHOW CURRENT QUESTION
    // =====================================================

    private void showQuestion() {


        // =================================================
        // CHECK QUESTION LIST
        // =================================================

        if (questionList == null ||
                questionList.isEmpty()) {

            showNoQuestions();

            return;
        }


        // =================================================
        // GET CURRENT QUESTION
        // =================================================

        Question question =
                questionList.get(
                        currentQuestionIndex
                );


        // =================================================
        // QUESTION NUMBER
        // =================================================

        TextView tvQuestionNumber =
                findViewById(
                        R.id.tvQuestionNumber
                );


        tvQuestionNumber.setText(
                "Question "
                        + (currentQuestionIndex + 1)
                        + " of "
                        + questionList.size()
        );


        // =================================================
        // QUESTION TEXT
        // =================================================

        TextView tvQuestion =
                findViewById(
                        R.id.tvQuestion
                );


        tvQuestion.setText(
                question.getQuestion()
        );


        // =================================================
        // OPTION TEXTS
        // =================================================

        TextView tvOptionAText =
                findViewById(
                        R.id.tvOptionAText
                );


        TextView tvOptionBText =
                findViewById(
                        R.id.tvOptionBText
                );


        TextView tvOptionCText =
                findViewById(
                        R.id.tvOptionCText
                );


        TextView tvOptionDText =
                findViewById(
                        R.id.tvOptionDText
                );


        tvOptionAText.setText(
                question.getOptionA()
        );


        tvOptionBText.setText(
                question.getOptionB()
        );


        tvOptionCText.setText(
                question.getOptionC()
        );


        tvOptionDText.setText(
                question.getOptionD()
        );


        // =================================================
        // QUESTION PROGRESS
        // =================================================

        int progress =
                ((currentQuestionIndex + 1) * 100)
                        / questionList.size();


        questionProgress.setProgress(
                progress
        );


        // =================================================
        // PREVIOUS BUTTON
        // =================================================

        btnPrevious.setEnabled(
                currentQuestionIndex > 0
        );


        // =================================================
        // NEXT / SUBMIT BUTTON
        // =================================================

        if (currentQuestionIndex ==
                questionList.size() - 1) {

            btnNext.setText(
                    "Submit Quiz"
            );

        } else {

            btnNext.setText(
                    "Next"
            );
        }


        btnNext.setEnabled(true);


        // =================================================
        // RESET OPTIONS
        // =================================================

        resetOption(
                cardOptionA
        );

        resetOption(
                cardOptionB
        );

        resetOption(
                cardOptionC
        );

        resetOption(
                cardOptionD
        );


        // =================================================
        // RESTORE SELECTED ANSWER
        // =================================================

        String selectedAnswer =
                question.getSelectedAnswer();


        if (selectedAnswer != null) {

            switch (selectedAnswer) {

                case "A":

                    selectOption(
                            cardOptionA
                    );

                    break;


                case "B":

                    selectOption(
                            cardOptionB
                    );

                    break;


                case "C":

                    selectOption(
                            cardOptionC
                    );

                    break;


                case "D":

                    selectOption(
                            cardOptionD
                    );

                    break;
            }
        }
    }


    // =====================================================
    // SELECT OPTION
    // =====================================================

    private void selectOption(
            MaterialCardView selectedCard) {


        // =================================================
        // RESET ALL OPTIONS
        // =================================================

        resetOption(
                cardOptionA
        );

        resetOption(
                cardOptionB
        );

        resetOption(
                cardOptionC
        );

        resetOption(
                cardOptionD
        );


        // =================================================
        // HIGHLIGHT SELECTED OPTION
        // =================================================

        selectedCard.setCardBackgroundColor(
                ContextCompat.getColor(
                        this,
                        R.color.questionOptionSelectedBg
                )
        );

        selectedCard.setStrokeColor(
                ContextCompat.getColor(
                        this,
                        R.color.questionOptionSelectedStroke
                )
        );

        // Re-apply the current theme colors to the option text as well.
        TextView[] optionTexts = {
                findViewById(R.id.tvOptionAText),
                findViewById(R.id.tvOptionBText),
                findViewById(R.id.tvOptionCText),
                findViewById(R.id.tvOptionDText),
                findViewById(R.id.tvOptionALetter),
                findViewById(R.id.tvOptionBLetter),
                findViewById(R.id.tvOptionCLetter),
                findViewById(R.id.tvOptionDLetter)
        };

        int optionTextColor =
                ContextCompat.getColor(
                        this,
                        R.color.subjectNameTextColor
                );

        for (TextView textView : optionTexts) {
            textView.setTextColor(optionTextColor);
        }


        // =================================================
        // SAVE ANSWER
        // =================================================

        Question currentQuestion =
                questionList.get(
                        currentQuestionIndex
                );


        if (selectedCard == cardOptionA) {

            currentQuestion.setSelectedAnswer(
                    "A"
            );

        } else if (selectedCard == cardOptionB) {

            currentQuestion.setSelectedAnswer(
                    "B"
            );

        } else if (selectedCard == cardOptionC) {

            currentQuestion.setSelectedAnswer(
                    "C"
            );

        } else if (selectedCard == cardOptionD) {

            currentQuestion.setSelectedAnswer(
                    "D"
            );
        }
    }


    // =====================================================
    // RESET OPTION
    // =====================================================

    private void resetOption(
            MaterialCardView card) {


        card.setCardBackgroundColor(
                ContextCompat.getColor(
                        this,
                        R.color.cardBgColor
                )
        );


        card.setStrokeColor(
                ContextCompat.getColor(
                        this,
                        R.color.topicCardStroke
                )
        );
    }


    // =====================================================
    // SUBMIT QUIZ
    // =====================================================

    private void submitQuiz() {

        int unansweredCount = 0;


        // =================================================
        // COUNT UNANSWERED
        // =================================================

        for (Question question :
                questionList) {

            if (question.getSelectedAnswer() == null) {

                unansweredCount++;
            }
        }


        // =================================================
        // UNANSWERED QUESTIONS
        // =================================================

        if (unansweredCount > 0) {

            new androidx.appcompat.app.AlertDialog.Builder(
                    this,
                    R.style.QuestionsDialogTheme
            )

                    .setTitle(
                            "Unanswered Questions"
                    )

                    .setMessage(
                            "You have "
                                    + unansweredCount
                                    + " unanswered question"
                                    + (
                                    unansweredCount > 1
                                            ? "s."
                                            : "."
                            )
                                    + "\n\nDo you want to submit the quiz anyway?"
                    )

                    .setNegativeButton(
                            "Continue Quiz",
                            null
                    )

                    .setPositiveButton(
                            "Submit Anyway",
                            (dialog, which) -> {

                                confirmSubmit();
                            }
                    )

                    .show();

        } else {

            confirmSubmit();
        }
    }


    // =====================================================
    // CONFIRM SUBMIT
    // =====================================================

    private void confirmSubmit() {

        new androidx.appcompat.app.AlertDialog.Builder(
                this,
                R.style.QuestionsDialogTheme
        )

                .setTitle(
                        "Submit Quiz"
                )

                .setMessage(
                        "Are you sure you want to submit the quiz?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Submit",
                        (dialog, which) -> {


                            // =================================
                            // CREATE QUIZ RESULT
                            // =================================

                            QuizResult result =
                                    new QuizResult(
                                            questionList,
                                            selectedTopicIds
                                    );


                            // =================================
                            // SAVE PRACTICE STATISTICS
                            // =================================

                            PracticeStatsManager.saveQuizResult(
                                    QuestionsActivity.this,
                                    result
                            );


                            // =================================
                            // SAVE RESULT FOR RESULT ACTIVITY
                            // =================================

                            QuizResultManager.setResult(
                                    result
                            );


                            // =================================
                            // OPEN RESULT ACTIVITY
                            // =================================

                            Intent intent =
                                    new Intent(
                                            QuestionsActivity.this,
                                            ResultActivity.class
                                    );


                            startActivity(
                                    intent
                            );


                            // =================================
                            // CLOSE QUESTIONS ACTIVITY
                            // =================================

                            finish();
                        }
                )

                .show();
    }


    // =====================================================
    // BACK PRESSED
    // =====================================================

    @Override
    public void onBackPressed() {

        getOnBackPressedDispatcher()
                .onBackPressed();
    }
}
