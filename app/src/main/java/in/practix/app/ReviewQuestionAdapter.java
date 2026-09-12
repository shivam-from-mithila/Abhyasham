package in.practix.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewQuestionAdapter
        extends RecyclerView.Adapter<ReviewQuestionAdapter.ReviewViewHolder> {

    private final List<Question> questionList;

    public ReviewQuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_review_question,
                        parent,
                        false
                );

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ReviewViewHolder holder,
            int position) {

        Question question = questionList.get(position);

        // Question number
        holder.tvQuestionNumber.setText(
                "Question " + (position + 1)
        );

        // Question
        holder.tvQuestion.setText(
                question.getQuestion()
        );

        // Options
        holder.tvOptionA.setText(
                "A. " + question.getOptionA()
        );

        holder.tvOptionB.setText(
                "B. " + question.getOptionB()
        );

        holder.tvOptionC.setText(
                "C. " + question.getOptionC()
        );

        holder.tvOptionD.setText(
                "D. " + question.getOptionD()
        );

        String selectedAnswer =
                question.getSelectedAnswer();

        String correctAnswer =
                question.getCorrectAnswer();

        // Determine result
        if (selectedAnswer == null ||
                selectedAnswer.isEmpty()) {

            // UNATTEMPTED

            holder.tvResultStatus.setText(
                    "Not Attempted"
            );

            holder.tvResultStatus.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.reviewUnattemptedText
                    )
            );

            holder.resultStatusContainer.setBackgroundResource(
                    R.drawable.bg_review_unattempted
            );

            holder.tvYourAnswer.setText(
                    "Your Answer: Not Attempted"
            );

            holder.tvCorrectAnswer.setText(
                    "Correct Answer: "
                            + getAnswerText(question, correctAnswer)
            );

        } else if (selectedAnswer.equals(correctAnswer)) {

            // CORRECT

            holder.tvResultStatus.setText(
                    "✓ Correct"
            );

            holder.tvResultStatus.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.reviewCorrectText
                    )
            );

            holder.resultStatusContainer.setBackgroundResource(
                    R.drawable.bg_review_correct
            );

            holder.tvYourAnswer.setText(
                    "Your Answer: "
                            + getAnswerText(question, selectedAnswer)
            );

            holder.tvCorrectAnswer.setText(
                    "Correct Answer: "
                            + getAnswerText(question, correctAnswer)
            );

        } else {

            // INCORRECT

            holder.tvResultStatus.setText(
                    "✕ Incorrect"
            );

            holder.tvResultStatus.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.reviewIncorrectText
                    )
            );

            holder.resultStatusContainer.setBackgroundResource(
                    R.drawable.bg_review_incorrect
            );

            holder.tvYourAnswer.setText(
                    "Your Answer: "
                            + getAnswerText(question, selectedAnswer)
            );

            holder.tvCorrectAnswer.setText(
                    "Correct Answer: "
                            + getAnswerText(question, correctAnswer)
            );
        }

        // Explanation
        holder.tvExplanation.setText(
                question.getExplanation()
        );
    }

    /**
     * Converts answer letter (A/B/C/D)
     * into the actual option text.
     */
    private String getAnswerText(
            Question question,
            String answer) {

        if (answer == null || answer.isEmpty()) {
            return "Not Attempted";
        }

        switch (answer) {

            case "A":
                return "A. " + question.getOptionA();

            case "B":
                return "B. " + question.getOptionB();

            case "C":
                return "C. " + question.getOptionC();

            case "D":
                return "D. " + question.getOptionD();

            default:
                return answer;
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ReviewViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvQuestionNumber;
        TextView tvQuestion;

        TextView tvOptionA;
        TextView tvOptionB;
        TextView tvOptionC;
        TextView tvOptionD;

        LinearLayout resultStatusContainer;

        TextView tvResultStatus;
        TextView tvYourAnswer;
        TextView tvCorrectAnswer;

        TextView tvExplanation;

        public ReviewViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvQuestionNumber =
                    itemView.findViewById(
                            R.id.tvReviewQuestionNumber
                    );

            tvQuestion =
                    itemView.findViewById(
                            R.id.tvReviewQuestion
                    );

            tvOptionA =
                    itemView.findViewById(
                            R.id.tvReviewOptionA
                    );

            tvOptionB =
                    itemView.findViewById(
                            R.id.tvReviewOptionB
                    );

            tvOptionC =
                    itemView.findViewById(
                            R.id.tvReviewOptionC
                    );

            tvOptionD =
                    itemView.findViewById(
                            R.id.tvReviewOptionD
                    );

            resultStatusContainer =
                    itemView.findViewById(
                            R.id.resultStatusContainer
                    );

            tvResultStatus =
                    itemView.findViewById(
                            R.id.tvResultStatus
                    );

            tvYourAnswer =
                    itemView.findViewById(
                            R.id.tvYourAnswer
                    );

            tvCorrectAnswer =
                    itemView.findViewById(
                            R.id.tvCorrectAnswer
                    );

            tvExplanation =
                    itemView.findViewById(
                            R.id.tvExplanation
                    );
        }
    }
}