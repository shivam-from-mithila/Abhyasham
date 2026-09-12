package in.practix.app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class SubjectAdapter
        extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final ArrayList<Subject> subjectList;

    // Subject ID -> Progress %
    private final Map<String, Integer> subjectProgressMap;


    // =========================================
    // Constructor
    // =========================================

    public SubjectAdapter(
            ArrayList<Subject> subjectList,
            Map<String, Integer> subjectProgressMap) {

        this.subjectList = subjectList;

        this.subjectProgressMap =
                subjectProgressMap;
    }


    // =========================================
    // Create ViewHolder
    // =========================================

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_subject,
                                parent,
                                false
                        );

        return new SubjectViewHolder(view);
    }


    // =========================================
    // Bind ViewHolder
    // =========================================

    @Override
    public void onBindViewHolder(
            @NonNull SubjectViewHolder holder,
            int position) {

        Subject subject =
                subjectList.get(position);


        // ====================================
        // Subject Name
        // ====================================

        holder.subjectName.setText(
                subject.getName()
        );


        // ====================================
        // Total Questions
        // ====================================

        holder.questionCount.setText(
                subject.getQuestions()
                        + " Questions"
        );


        // ====================================
        // Subject Progress
        // ====================================

        int progress = 0;


        if (subjectProgressMap != null) {

            Integer savedProgress =
                    subjectProgressMap.get(
                            subject.getId()
                    );


            if (savedProgress != null) {

                progress =
                        savedProgress;
            }
        }


        // Safety

        if (progress < 0) {

            progress = 0;
        }


        if (progress > 100) {

            progress = 100;
        }


        holder.subjectProgress.setProgress(
                progress
        );


        holder.progressText.setText(
                progress + "%"
        );


        // ====================================
        // Subject Icon
        // ====================================

        Glide.with(
                        holder.itemView.getContext()
                )
                .load(subject.getIconUrl())
                .placeholder(R.drawable.state)
                .error(R.drawable.state)
                .into(holder.subjectIcon);


        // ====================================
        // Abhyasham Button
        // ====================================

        holder.startAbhyasham.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            v.getContext(),
                            TopicsActivity.class
                    );


            intent.putExtra(
                    "SUBJECT_ID",
                    subject.getId()
            );


            intent.putExtra(
                    "SUBJECT_NAME",
                    subject.getName()
            );


            v.getContext().startActivity(
                    intent
            );
        });
    }


    // =========================================
    // Item Count
    // =========================================

    @Override
    public int getItemCount() {

        return subjectList.size();
    }


    // =========================================
    // ViewHolder
    // =========================================

    public static class SubjectViewHolder
            extends RecyclerView.ViewHolder {

        ImageView subjectIcon;

        TextView subjectName;
        TextView questionCount;
        TextView progressText;

        ProgressBar subjectProgress;

        Button startAbhyasham;


        public SubjectViewHolder(
                @NonNull View itemView) {

            super(itemView);


            subjectIcon =
                    itemView.findViewById(
                            R.id.subjectIcon
                    );


            subjectName =
                    itemView.findViewById(
                            R.id.subjectName
                    );


            questionCount =
                    itemView.findViewById(
                            R.id.questionCount
                    );


            progressText =
                    itemView.findViewById(
                            R.id.progressText
                    );


            subjectProgress =
                    itemView.findViewById(
                            R.id.subjectProgress
                    );


            startAbhyasham =
                    itemView.findViewById(
                            R.id.startAbhyasham
                    );
        }
    }
}